package com.zhanghe.poi.autoconfig.config;

import com.zhanghe.poi.api.ExcelController;
import com.zhanghe.poi.autoconfig.annotation.ExcelConfigurationScanner;
import com.zhanghe.poi.autoconfig.annotation.ExcelGroupEntity;
import com.zhanghe.poi.autoconfig.entity.BeanExcelEntity;
import com.zhanghe.poi.autoconfig.entity.ExcelEntity;
import com.zhanghe.poi.util.ExcelMapperScannerTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author: ZhangHe
 * @since: 2020/5/26 17:32
 */
//@Configuration
@Slf4j
public class ExcelGroupEntityAutoConfiguration  {

    public static class GroupEntityRegisterConfiguration implements BeanFactoryAware, ResourceLoaderAware, ImportBeanDefinitionRegistrar {
        private BeanFactory beanFactory;

        private ResourceLoader resourceLoader;


        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory= beanFactory;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            try {
//                registerApiConfiguration(registry);

                Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(ExcelConfigurationScanner.class.getCanonicalName());

                String[] ps = (String[]) annotationAttributes.get("packages");


                //获取工厂自动配置基础包
                List<String> packages = Arrays.asList(ps);

                Map<String, List<ExcelEntity>> excelEntities = scannerExcelGroupEntity(packages);

                ExcelMapperScannerTemplate bean = this.beanFactory.getBean(ExcelMapperScannerTemplate.BEAN_NAME,ExcelMapperScannerTemplate.class);
                //初始化模板位置
                for (List<ExcelEntity> value : excelEntities.values()) {
                    for (ExcelEntity excelEntity : value) {
                        excelEntity.setTemplateLocation(bean.getTemplateLocation());
                    }
                }
                bean.setExcelEntitiesMap(excelEntities);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private Map<String, List<ExcelEntity>> scannerExcelGroupEntity(List<String> packages) throws ClassNotFoundException {
            ClassPathAnnotationScannerClass classPathAnnotationScannerClass = new ClassPathAnnotationScannerClass();
            classPathAnnotationScannerClass.setResourceLoader(this.resourceLoader);
            classPathAnnotationScannerClass.init(ExcelGroupEntity.class,packages);
            return BeanExcelEntity.getExcelEntities(classPathAnnotationScannerClass.getScannerBeans());
        }
        private void registerApiConfiguration(BeanDefinitionRegistry registry) throws ClassNotFoundException {
            ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(registry);
            classPathBeanDefinitionScanner.scan(ExcelController.class.getPackage().getName());
        }
    }


}
