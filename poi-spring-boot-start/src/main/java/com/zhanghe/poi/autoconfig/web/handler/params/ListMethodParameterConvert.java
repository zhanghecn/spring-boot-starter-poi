package com.zhanghe.poi.autoconfig.web.handler.params;


import com.zhanghe.poi.util.excel.mapper.ExcelMapper;
import com.zhanghe.poi.util.excel.mapper.ExcelMappersEntity;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;

import java.util.LinkedList;
import java.util.List;

public class ListMethodParameterConvert implements ExcelMethodParamsHandler {
    @Override
    public boolean canConvert(MethodParameter methodParameter, Object value) {
        if(isExcelMappersEntity(value)  && isAssignable(methodParameter)){
            return true;
        }
        return false;
    }

    @Override
    public Object convert(MethodParameter methodParameter, Object value) {
        ExcelMappersEntity excelMappersEntity = (ExcelMappersEntity) value;
        List list = new LinkedList();
        List<ExcelMapper> excelMappers = excelMappersEntity.getAllExcelMappers();
        for (ExcelMapper excelMapper : excelMappers) {
            List objs = excelMapper.list(excelMappersEntity.getExcelEntity());
            list.addAll(objs);
        }
        return list;
    }

    @Override
    public Class<?> getRawClass(MethodParameter methodParameter) {
        return getRawClassByList(methodParameter);
    }

    @Override
    public boolean isAssignable(MethodParameter methodParameter) {
        return ClassUtils.isAssignable(List.class,methodParameter.getParameterType());
    }
}
