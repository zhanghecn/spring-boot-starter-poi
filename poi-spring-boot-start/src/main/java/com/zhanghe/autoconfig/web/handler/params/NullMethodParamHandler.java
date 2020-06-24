package com.zhanghe.autoconfig.web.handler.params;

import org.springframework.core.MethodParameter;

public class NullMethodParamHandler implements ExcelMethodParamsHandler {
    @Override
    public Object convert(MethodParameter methodParameter, Object value) {
        return null;
    }

    @Override
    public boolean isAssignable(MethodParameter methodParameter) {
        return true;
    }
}
