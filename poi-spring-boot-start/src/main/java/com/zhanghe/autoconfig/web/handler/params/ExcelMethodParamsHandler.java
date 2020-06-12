package com.zhanghe.autoconfig.web.handler.params;

import com.zhanghe.util.excel.mapper.ExcelMappersEntity;
import org.springframework.core.MethodParameter;

public interface ExcelMethodParamsHandler {

    boolean canConvert(MethodParameter methodParameter, Object value);

    Object convert(MethodParameter methodParameter, Object value);

    default boolean isExcelMappersEntity(Object val){
        return val instanceof ExcelMappersEntity;
    }
}
