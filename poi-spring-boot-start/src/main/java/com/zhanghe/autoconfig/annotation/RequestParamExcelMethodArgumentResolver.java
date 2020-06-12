package com.zhanghe.autoconfig.annotation;

import com.zhanghe.autoconfig.entity.ExcelEntity;
import com.zhanghe.util.ExcelMapperUtil;
import com.zhanghe.util.SpringContextHelper;
import com.zhanghe.util.excel.mapper.ExcelMappersEntity;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.MultipartResolutionDelegate;

import java.util.List;


public class RequestParamExcelMethodArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        ExcelParam requestParam = parameter.getParameterAnnotation(ExcelParam.class);

        return requestParam!=null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        ExcelParam requestParam = parameter.getParameterAnnotation(ExcelParam.class);

        //获取配置名
        String configName = requestParam.configName();

        String paramName = requestParam.name();

        //获取配置类
        Class<?> aClass = requestParam.configClass();

        ExcelMapperUtil excelMapperUtil = ExcelMapperUtil.getExcelMapperUtil(SpringContextHelper.applicationContext);

        //获取配置实体
        List<ExcelEntity> excelEntities;

        //按照配置名称或者配置类读取配置
        if(StringUtils.isEmpty(configName)){
            excelEntities  = excelMapperUtil.loadGroups(aClass);
        }else{
            excelEntities  = excelMapperUtil.loadGroups(configName);
        }

        //解析成multipart 的请求
        MultipartRequest multipartRequest = MultipartResolutionDelegate.resolveMultipartRequest(webRequest);

        //获取文件
        List<MultipartFile> files = multipartRequest.getFiles(paramName);

        ExcelMappersEntity excelMappersEntity = ExcelMappersEntity.multipartFileMappersEntity(excelEntities, files);


        return null;
    }
}
