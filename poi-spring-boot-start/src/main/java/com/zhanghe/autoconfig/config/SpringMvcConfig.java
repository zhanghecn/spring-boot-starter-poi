package com.zhanghe.autoconfig.config;


import com.zhanghe.autoconfig.annotation.ExcelExportMethodReturnHandler;
import com.zhanghe.util.ExcelMapperUtil;
import com.zhanghe.util.SpringContextHelper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;

/**
 * 主要还是配置导出的处理
 */
@Configuration
@ConditionalOnBean(RequestMappingHandlerAdapter.class)
@Order(Integer.MAX_VALUE)
public class SpringMvcConfig  implements InitializingBean {
   private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    /**
     * 获取ExcelMapperUtil Bean的工厂
     * @return
     */
    public FactoryBean<ExcelMapperUtil> factoryBean(){
        return new FactoryBean<ExcelMapperUtil>() {
            @Override
            public ExcelMapperUtil getObject() throws Exception {
                return SpringContextHelper.applicationContext.getBean(ExcelMapperUtil.class);
            }

            @Override
            public Class<?> getObjectType() {
                return ExcelMapperUtil.class;
            }
        };
    }

    /**
     * 获取请求映射处理适配器
     * @param requestMappingHandlerAdapter
     */
    public SpringMvcConfig(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
    }

    /**
     * 按照顺序把导出放在第一个处理
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ExcelExportMethodReturnHandler excelExportMethodReturnHandler = new ExcelExportMethodReturnHandler(factoryBean());
        ArrayList<HandlerMethodReturnValueHandler> list = new ArrayList<>();
        list.add(excelExportMethodReturnHandler);
        list.addAll(this.requestMappingHandlerAdapter.getReturnValueHandlers());
        this.requestMappingHandlerAdapter.setReturnValueHandlers(list);
    }
}
