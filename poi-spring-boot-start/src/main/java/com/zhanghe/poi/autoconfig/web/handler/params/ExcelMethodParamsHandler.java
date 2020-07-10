package com.zhanghe.poi.autoconfig.web.handler.params;

import com.zhanghe.poi.util.excel.mapper.ExcelMappersEntity;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;

public interface ExcelMethodParamsHandler {
    /**
     * 根据value进一步判断是否可以转换
     * @param methodParameter 方法参数
     * @param value 值
     * @return 是否可以转换
     */
    default   boolean canConvert(MethodParameter methodParameter, Object value){
        return isAssignable(methodParameter);
    }

    Object convert(MethodParameter methodParameter, Object value);

    default boolean isExcelMappersEntity(Object val){
        return val instanceof ExcelMappersEntity;
    }


    default Class<?> getRawClass(MethodParameter methodParameter){
        return Class.class;
    }

    default Class<?> getRawClassByList(MethodParameter methodParameter){
        ResolvableType resolvableType = ResolvableType.forMethodParameter(methodParameter);
        return resolvableType.getGeneric(0).getRawClass();
    }

    /**
     * 是否可以转换这个类型
     * @param methodParameter 方法参数
     * @return 是否可以分配到这个类型
     */
    boolean isAssignable(MethodParameter methodParameter);
}
