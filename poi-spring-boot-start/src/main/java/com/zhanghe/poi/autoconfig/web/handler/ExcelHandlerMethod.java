package com.zhanghe.poi.autoconfig.web.handler;

import com.zhanghe.poi.autoconfig.web.handler.params.ExcelMethodParamsHandler;
import com.zhanghe.poi.autoconfig.web.handler.params.ExcelParamHandlerProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ExcelHandlerMethod extends ServletInvocableHandlerMethod implements ApplicationContextAware {
   //自定义的value值
    private Object[] values;

    private ApplicationContext applicationContext;

    protected ExcelParamHandlerProvider instance = ExcelParamHandlerProvider.getInstance();

    public Object[] getValues() {
        return values;
    }
    public static ExcelHandlerMethod getItemMethodHandler(Object handler, Method method,Object... values){
        ExcelHandlerMethod excelHandlerMethod = new ExcelHandlerMethod(handler, method);
        excelHandlerMethod.values = values;
        return excelHandlerMethod;
    }
    public static ExcelHandlerMethod getItemMethodHandler(HandlerMethod handlerMethod, List<ExcelMethodParamsHandler> excelMethodParamsHandlers, Object... values){
        ExcelHandlerMethod excelHandlerMethod = new ExcelHandlerMethod(handlerMethod);
        excelHandlerMethod.values = values;
        return excelHandlerMethod;
    }
    private ExcelHandlerMethod(Object handler, Method method) {
        super(handler, method);
    }

    private ExcelHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }




    @Override
    protected Object[] getMethodArgumentValues(NativeWebRequest request, ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {


        //获取方法类型
        MethodParameter[] methodParameters = super.getMethodParameters();
        //获取除了转换excel对象的参数
        Object[] methodArgumentValues = super.getMethodArgumentValues(request, mavContainer, providedArgs);
        //创建加上excel对象参数的数量
        Object[] methodArgumentValuesAdd = new Object[methodArgumentValues.length+values.length];
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            MethodParameter methodParameter = methodParameters[i];
            if(instance.canConvert(methodParameter,value)){
                methodArgumentValuesAdd[i] = instance.convert(methodParameter,value);
            }else{
                methodArgumentValuesAdd[i] = value;
            }
        }
        //拷贝进去
        System.arraycopy(methodArgumentValues,0,methodArgumentValuesAdd,values.length,methodArgumentValues.length);
        return methodArgumentValuesAdd;
    }




    @Override
    public MethodParameter[] getMethodParameters() {
        MethodParameter[] methodParameters = super.getMethodParameters();
        if(methodParameters==null){
            return methodParameters;
        }
        //腾出空间
        MethodParameter[] copyOfRange = Arrays.copyOfRange(methodParameters, values.length, methodParameters.length);
        return copyOfRange;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
