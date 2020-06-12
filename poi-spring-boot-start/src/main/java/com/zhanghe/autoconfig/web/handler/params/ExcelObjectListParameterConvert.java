package com.zhanghe.autoconfig.web.handler.params;


import com.zhanghe.autoconfig.entity.ExcelEntity;
import com.zhanghe.autoconfig.entity.ExcelObjectData;
import com.zhanghe.autoconfig.entity.ExcelObjectList;
import com.zhanghe.util.excel.mapper.ExcelMapper;
import com.zhanghe.util.excel.mapper.ExcelMappersEntity;
import com.zhanghe.util.excel.mapper.FileExcelMapperInfo;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

public class ExcelObjectListParameterConvert implements ExcelMethodParamsHandler{

    @Override
    public boolean canConvert(MethodParameter methodParameter, Object value) {
        Class<?> parameterType = methodParameter.getParameterType();
        if(isExcelMappersEntity(value)  && ClassUtils.isAssignable(ExcelObjectList.class,parameterType)){
            return true;
        }
        return false;
    }

    @Override
    public Object convert(MethodParameter methodParameter, Object value) {
        ExcelMappersEntity excelMappersEntity = (ExcelMappersEntity) value;
        List<FileExcelMapperInfo> excelMappers = excelMappersEntity.getExcelMappers();
        ExcelEntity excelEntity = excelMappersEntity.getExcelEntity();
        ExcelObjectList excelObjectList = new ExcelObjectList();
        for (FileExcelMapperInfo excelMapper : excelMappers) {
            MultipartFile multipartFile = excelMapper.getMultipartFile();
            ExcelMapper[] excelMappers1 = excelMapper.getExcelMappers();
            List linkedList  = new LinkedList();
            for (ExcelMapper mapper : excelMappers1) {
                List list = mapper.list(excelEntity);
                linkedList.addAll(list);
            }
            ExcelObjectData excelObjectData = new ExcelObjectData(multipartFile,linkedList);
            excelObjectList.add(excelObjectData);
        }
        return excelObjectList;
    }

    @Override
    public Class<?> getRawClass(MethodParameter methodParameter) {
        return getRawClassByList(methodParameter);
    }

    @Override
    public boolean isAssignable(MethodParameter methodParameter) {
        return  ClassUtils.isAssignable(ExcelObjectList.class,methodParameter.getParameterType());
    }
}
