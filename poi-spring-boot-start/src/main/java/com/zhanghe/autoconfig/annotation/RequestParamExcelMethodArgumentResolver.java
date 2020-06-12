package com.zhanghe.autoconfig.annotation;

import com.zhanghe.autoconfig.entity.ExcelEntity;
import com.zhanghe.autoconfig.web.handler.params.ExcelMethodParamsHandler;
import com.zhanghe.autoconfig.web.handler.params.ExcelParamHandlerProvider;
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
import java.util.stream.Collectors;


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
        ExcelParamHandlerProvider instance = ExcelParamHandlerProvider.getInstance();

        ExcelParam requestParam = parameter.getParameterAnnotation(ExcelParam.class);

        //获取配置名
        String configName = requestParam.configName();

        String paramName = requestParam.name();


        ExcelMapperUtil excelMapperUtil = ExcelMapperUtil.getExcelMapperUtil(SpringContextHelper.applicationContext);

        //获取配置实体
        List<ExcelEntity> excelEntities;

        //按照配置名称或者配置类读取配置
        if(StringUtils.isEmpty(configName)){
            //获取配置类
            ExcelMethodParamsHandler excelMethodParamsHandler = instance.getExcelMethodParameterConverter(parameter).orElseThrow(() -> new IllegalArgumentException("@ExcelParam 未知的注入类型,必须给配置名称 [configName]"));
            //获取配置类型
            Class<?> aClass = excelMethodParamsHandler.getRawClass(parameter);
            //获取配置
            excelEntities  = excelMapperUtil.loadGroups(aClass);
        }else{
            excelEntities  = excelMapperUtil.loadGroups(configName);
        }

        //解析成multipart 的请求
        MultipartRequest multipartRequest = MultipartResolutionDelegate.resolveMultipartRequest(webRequest);

        List<MultipartFile> files;

        if(!StringUtils.isEmpty(paramName)){
            files = multipartRequest.getFiles(paramName);
        }else{
            files = multipartRequest.getFileMap().values().parallelStream().collect(Collectors.toList());
        }

        //获取文件
        ExcelMappersEntity excelMappersEntity = ExcelMappersEntity.multipartFileMappersEntity(excelEntities, files);

        return instance.convert(parameter,excelMappersEntity);
    }
}
