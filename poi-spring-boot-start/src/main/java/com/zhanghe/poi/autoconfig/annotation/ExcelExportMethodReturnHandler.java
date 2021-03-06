package com.zhanghe.poi.autoconfig.annotation;


import com.zhanghe.poi.autoconfig.entity.ExcelEntity;
import com.zhanghe.poi.autoconfig.entity.ExcelGroupSheets;
import com.zhanghe.poi.util.excel.mapper.ExcelMapper;
import com.zhanghe.poi.util.ExcelMapperUtil;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ExcelExportMethodReturnHandler implements HandlerMethodReturnValueHandler
{
    private FactoryBean<ExcelMapperUtil> excelMapperUtilFactoryBean;

    public ExcelExportMethodReturnHandler(FactoryBean<ExcelMapperUtil> excelMapperUtilFactoryBean) {
        this.excelMapperUtilFactoryBean = excelMapperUtilFactoryBean;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (List.class.isAssignableFrom(returnType.getParameterType())||
                ClassUtils.isAssignable(ExcelGroupSheets.class,returnType.getParameterType()))&&
                returnType.hasMethodAnnotation(ExcelExport.class);
    }

    /**
     * @param returnValue 返回值
     * @param returnType    返回类型
     * @param mavContainer  mav
     * @param webRequest    请求
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);
        ExcelMapperUtil excelMapperUtil = excelMapperUtilFactoryBean.getObject();
        ExcelExport mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(returnType.getMethod(), ExcelExport.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        setContentType(mergedAnnotation.exportFileName(), response);

        ServletOutputStream outputStream = response.getOutputStream();

        Class<?> parameterType = returnType.getParameterType();
        //打包成zip
        if(mergedAnnotation.packageZIP()&& ClassUtils.isAssignable(List.class,parameterType)){

            ResolvableType resolvableType = ResolvableType.forMethodParameter(returnType);
            ResolvableType generic = resolvableType.getGeneric(0);
            Type type = generic.getType();
            Assert.isTrue(ClassUtils.isAssignable(ExcelGroupSheets.class, (Class<?>) type),"只支持List<ExcelGroupSheets>打包");
            //打包序号
            int i = 0;
            //文件名
            String exportFileName = mergedAnnotation.exportFileName();
            List<ExcelGroupSheets> res = (List<ExcelGroupSheets>) returnValue;
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (ExcelGroupSheets re : res) {
                String file;
                if(StringUtils.hasText(re.getFileName())){
                    file = re.getFileName() + ".xlsx";
                }else{
                    file = exportFileName + (++i) + ".xlsx";
                }
                zipOutputStream.putNextEntry(new ZipEntry(file));
                writeExcelGroupSheets(re,mergedAnnotation,excelMapperUtil,byteArrayOutputStream);
                zipOutputStream.write(byteArrayOutputStream.toByteArray());
                byteArrayOutputStream.reset();
            }
            IOUtils.closeQuietly(zipOutputStream);
        }else{
            if(ClassUtils.isAssignable(List.class,parameterType)){
                List<?> list = (List<?>) returnValue;
                List<ExcelEntity> excelEntities = getExcelEntities(mergedAnnotation, excelMapperUtil);
                if(!ObjectUtils.isEmpty(excelEntities)){
                    ExcelEntity excelEntity = excelEntities.get(0);
                    ExcelMapper excelMapper = ExcelMapper.getExcelMapper(excelEntity);
                    excelMapper.writer(excelEntity,list,outputStream);
                }
            }else{
                writeExcelGroupSheets((ExcelGroupSheets) returnValue,mergedAnnotation,excelMapperUtil,outputStream);
            }
        }


    }

    protected void writeExcelGroupSheets(ExcelGroupSheets excelGroupSheets, ExcelExport mergedAnnotation, ExcelMapperUtil excelMapperUtil, OutputStream outputStream) throws IOException {

        List<ExcelEntity> loadGroups = getExcelEntities(mergedAnnotation, excelMapperUtil);

        ExcelMapper excelMapper = ExcelMapper.getExcelMapper(loadGroups);
        if(excelMapper!=null){
            excelMapper.writer(loadGroups,excelGroupSheets,outputStream);
        }
    }

    private List<ExcelEntity> getExcelEntities(ExcelExport mergedAnnotation, ExcelMapperUtil excelMapperUtil) {
        List<ExcelEntity> loadGroups;

        if(StringUtils.isEmpty(mergedAnnotation.configBeanName())){
            loadGroups = excelMapperUtil.loadGroups(mergedAnnotation.exportClass());
        }else{
            loadGroups = excelMapperUtil.loadGroups(mergedAnnotation.configBeanName());
        }
        return loadGroups;
    }

    public static void setContentType(String exportFileName, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        exportFileName = response.encodeURL(new String(exportFileName.getBytes(),"iso8859-1"));			//保存的文件名,必须和页面编码一致,否则乱码
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=" + exportFileName);
    }


}
