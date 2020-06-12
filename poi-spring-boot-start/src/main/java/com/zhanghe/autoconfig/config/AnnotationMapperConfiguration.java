package com.zhanghe.autoconfig.config;

import com.zhanghe.autoconfig.annotation.ExcelConfigurationScanner;
import com.zhanghe.util.ExcelMapperScannerTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 *注册 ExcelMapperScannerTemplate
 */
@Slf4j
public class AnnotationMapperConfiguration implements ImportBeanDefinitionRegistrar, BeanFactoryAware{
   private ConfigurableListableBeanFactory beanFactory;
   @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(ExcelConfigurationScanner.class.getName());
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationAttributes);
        String value = attributes.getString("templateLocation");
        ExcelMapperScannerTemplate excelMapperScannerTemplate = new ExcelMapperScannerTemplate(value);
        excelMapperScannerTemplate.setBeanFactory(this.beanFactory);

       this.beanFactory.registerSingleton(ExcelMapperScannerTemplate.BEAN_NAME,excelMapperScannerTemplate);
   }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

}
