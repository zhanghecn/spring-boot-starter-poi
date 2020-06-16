package com.zhanghe.autoconfig.web;

import com.zhanghe.autoconfig.web.handler.ExcelHandlerMethod;
import com.zhanghe.autoconfig.web.handler.params.ExcelMethodParamsHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 请求路径匹配处理
 */
public class URIPathMappingHandlerAdapter extends RequestMappingHandlerAdapter {
    RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    public URIPathMappingHandlerAdapter(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
        init();
    }

    ThreadLocal<Object[]> threadLocal = new ThreadLocal<>();

    public void setCurrentValues(Object[] values){
        threadLocal.set(values);
    }

    /**
     * 创建执行这个路径的方法  在RequestMappingHandlerAdapter invokeHandlerMethod的模板方法里面使用
     * @param handlerMethod
     * @return
     */
    @Override
    protected ServletInvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
        List<ExcelMethodParamsHandler> excelMethodParamsHandlers = getExcelMethodParamsHandlers();
        ExcelHandlerMethod itemMethodHandler = ExcelHandlerMethod.getItemMethodHandler(handlerMethod, excelMethodParamsHandlers, threadLocal.get());
        //使用过就删除，防止引用，导致内存溢出
        threadLocal.remove();
        return itemMethodHandler;
    }

    /**
     * 获取所有excel参数解析 如SheetHandlerList 等
     * @return
     */
    private List<ExcelMethodParamsHandler> getExcelMethodParamsHandlers() {
        ApplicationContext applicationContext = getApplicationContext();
        Map<String, ExcelMethodParamsHandler> beansOfType = applicationContext.getBeansOfType(ExcelMethodParamsHandler.class);
        Collection<ExcelMethodParamsHandler> values = beansOfType.values();
        return new LinkedList<>(values);
    }

    /**
     * 参数解析和返回值解析
     */
    public void init(){
        setArgumentResolvers(requestMappingHandlerAdapter.getArgumentResolvers());
        setReturnValueHandlers(requestMappingHandlerAdapter.getReturnValueHandlers());
    }

}
