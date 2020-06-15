package com.zhanghe.poi.autoconfig.config;


import com.zhanghe.poi.api.ExcelController;
import com.zhanghe.poi.autoconfig.annotation.ExcelExportMethodReturnHandler;
import com.zhanghe.poi.autoconfig.annotation.RequestParamExcelMethodArgumentResolver;
import com.zhanghe.poi.util.ExcelMapperUtil;
import com.zhanghe.poi.util.SpringContextHelper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 主要还是配置导出的处理
 */
@Configuration
@ConditionalOnClass({DispatcherServlet.class})
@AutoConfigureAfter({ ExcelHelperPropertyAutoConfiguration.class, DispatcherServletAutoConfiguration.class})
@ConditionalOnMissingBean(SpringMvcExcelParamConfig.class)
public class SpringMvcExcelParamConfig implements InitializingBean {
   private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

   @Bean("excelAutoApi")
   public ExcelController excelController(){
       return new ExcelController();
   }
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


    public SpringMvcExcelParamConfig(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
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

    /**
     * 导入解析
     * @throws Exception
     */
    @Configuration
    public static class SpringMVCArgumentAutoConfig implements  WebMvcConfigurer{

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new RequestParamExcelMethodArgumentResolver());
        }
    }
}
