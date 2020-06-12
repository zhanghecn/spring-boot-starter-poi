package com.zhanghe.autoconfig.web.handler.params;

import com.zhanghe.util.SpringContextHelper;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

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
        return getExcelMethodParameterConverter(methodParameter,value).convert(methodParameter,value);
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

    public static synchronized ExcelParamHandlerProvider getInstance(){
        if(ObjectUtils.isEmpty(excelParamHandlerProvider)){
            excelParamHandlerProvider = new ExcelParamHandlerProvider();
            excelParamHandlerProvider.addDefaultExcelMethodParamsHandlers();
            excelParamHandlerProvider.addSpringBeanExcelMethodParamsHandlers();
        }
        return excelParamHandlerProvider;
    }
}
