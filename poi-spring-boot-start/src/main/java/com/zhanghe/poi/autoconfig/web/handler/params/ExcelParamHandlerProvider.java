package com.zhanghe.poi.autoconfig.web.handler.params;

import com.zhanghe.poi.util.SpringContextHelper;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 Created by ZhangHe
 ExcelParamHandlerProvider 提供参数处理解析集合
 */
public class ExcelParamHandlerProvider implements  ExcelMethodParamsHandler{
    private static ExcelParamHandlerProvider excelParamHandlerProvider;

    public List<ExcelMethodParamsHandler> excelMethodParamsHandlers;

    public List<ExcelMethodParamsHandler> getExcelMethodParamsHandlers() {
        return excelMethodParamsHandlers;
    }

    public void setExcelMethodParamsHandlers(List<ExcelMethodParamsHandler> excelMethodParamsHandlers) {
        this.excelMethodParamsHandlers = excelMethodParamsHandlers;
    }

    protected void addDefaultExcelMethodParamsHandlers() {
        List<ExcelMethodParamsHandler> list = new ArrayList<>();
        list.add(new ExcelGroupListParamsConvert());
        list.add(new SheetHandlerListParameterConvert());
        list.add(new ExcelObjectListParameterConvert());
        list.add(new ListMethodParameterConvert());
        this.excelMethodParamsHandlers = list;
    }

    protected void addSpringBeanExcelMethodParamsHandlers() {
        List<ExcelMethodParamsHandler> beans = SpringContextHelper.getBeans(ExcelMethodParamsHandler.class);
        addExcelMethodParamsHandlers(beans);
    }

    protected void addExcelMethodParamsHandlers(List<ExcelMethodParamsHandler> excelMethodParamsHandlers) {
        if(excelMethodParamsHandlers==null||excelMethodParamsHandlers.isEmpty()){
            return;
        }
        this.excelMethodParamsHandlers.addAll(excelMethodParamsHandlers);
    }
    @Override
    public boolean canConvert(MethodParameter methodParameter, Object value) {
        return getExcelMethodParameterConverter(methodParameter,value)!=null;
    }

    @Override
    public Object convert(MethodParameter methodParameter, Object value) {
        return Optional.ofNullable(getExcelMethodParameterConverter(methodParameter,value))
                .orElseGet(NullMethodParamHandler::new)
                .convert(methodParameter,value);
    }

    @Override
    public boolean isAssignable(MethodParameter methodParameter) {
        return false;
    }

    public ExcelMethodParamsHandler getExcelMethodParameterConverter(MethodParameter methodParameter, Object val){
        List<ExcelMethodParamsHandler> excelMethodParamsHandlers = this.excelMethodParamsHandlers;
        if(!(excelMethodParamsHandlers==null||excelMethodParamsHandlers.isEmpty()||val==null)) {
            for (ExcelMethodParamsHandler excelMethodParamsHandler : excelMethodParamsHandlers) {
                boolean b = excelMethodParamsHandler.canConvert(methodParameter, val);
                if (b) {
                    return excelMethodParamsHandler;
                }
            }
        }
        return null;
    }

    public Optional<ExcelMethodParamsHandler> getExcelMethodParameterConverter(MethodParameter methodParameter){
        Optional<ExcelMethodParamsHandler> first = this.excelMethodParamsHandlers.stream().filter((x) -> x.isAssignable(methodParameter)).findFirst();
        return first;
    }

    public static synchronized ExcelParamHandlerProvider getInstance(){
        if(ObjectUtils.isEmpty(excelParamHandlerProvider)){
            excelParamHandlerProvider = new ExcelParamHandlerProvider();
            excelParamHandlerProvider.addDefaultExcelMethodParamsHandlers();
            excelParamHandlerProvider.addSpringBeanExcelMethodParamsHandlers();
        }
        return excelParamHandlerProvider;
    }
}
