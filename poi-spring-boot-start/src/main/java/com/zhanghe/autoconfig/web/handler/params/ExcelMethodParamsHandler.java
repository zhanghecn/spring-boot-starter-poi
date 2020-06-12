package com.zhanghe.autoconfig.web.handler.params;

import com.zhanghe.util.excel.mapper.ExcelMappersEntity;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;

public interface ExcelMethodParamsHandler {
    /**
     * 根据value进一步判断是否可以转换
     * @param methodParameter
     * @param value
     * @return
     */
    default   boolean canConvert(MethodParameter methodParameter, Object value){
        return isAssignable(methodParameter);
    }

    Object convert(MethodParameter methodParameter, Object value);

    default boolean isExcelMappersEntity(Object val){
        return val instanceof ExcelMappersEntity;
    }

    /**
     * 获取原类 对于@ExcelParam 自动读取参数类使用
     * @return
     */
    default Class<?> getRawClass(MethodParameter methodParameter){
        return Class.class;
    }

    /**
     * 获取原类 对于@ExcelParam 自动读取参数类使用
     * @return
     */
    default Class<?> getRawClassByList(MethodParameter methodParameter){
        ResolvableType resolvableType = ResolvableType.forMethodParameter(methodParameter);
        return resolvableType.getGeneric(0).getRawClass();
    }

    /**
     * 是否可以转换这个类型
     * @param methodParameter
     * @return
     */
    boolean isAssignable(MethodParameter methodParameter);
}
