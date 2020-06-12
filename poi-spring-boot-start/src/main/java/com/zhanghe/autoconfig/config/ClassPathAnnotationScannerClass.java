package com.zhanghe.autoconfig.config;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 注解扫描类
 * @Author: ZhangHe
 * @Date: 2020/5/25 10:34
 */
public class ClassPathAnnotationScannerClass extends ClassPathBeanDefinitionScanner {
    public ClassPathAnnotationScannerClass() {

        super(new EmptyBeanDefinitionRegistry(),false);
    }

    public static class EmptyBeanDefinitionRegistry implements BeanDefinitionRegistry {

        @Override
        public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {

        }

        @Override
        public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {

        }

        @Override
        public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
            return null;
        }

        @Override
        public boolean containsBeanDefinition(String beanName) {
            return false;
        }

        @Override
        public String[] getBeanDefinitionNames() {
            return new String[0];
        }

        @Override
        public int getBeanDefinitionCount() {
            return 0;
        }

        @Override
        public boolean isBeanNameInUse(String beanName) {
            return false;
        }

        @Override
        public void registerAlias(String name, String alias) {

        }

        @Override
        public void removeAlias(String alias) {

        }

        @Override
        public boolean isAlias(String name) {
            return false;
        }

        @Override
        public String[] getAliases(String name) {
            return new String[0];
        }
    }
    private Class<? extends Annotation> annotationClass;
    
    private List<GenericBeanDefinition> scannerBeans = new ArrayList<>();
    
    public void registerFilters() {

        // if specified, use the given annotation and / or marker interface
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        }


        // exclude package-info.java
        addExcludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                String className = metadataReader.getClassMetadata().getClassName();
                return className.endsWith("package-info");
            }
        });
    }

    /**
     * 初始化扫包
     * @param annotationClass
     * @param packages
     */
    public void init(Class<? extends Annotation> annotationClass,List<String> packages){
        setAnnotationClass(annotationClass);
        registerFilters();
        Set<BeanDefinitionHolder> beanDefinitionHolders = doScan(StringUtils.toStringArray(packages));
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();
            scannerBeans.add((GenericBeanDefinition) beanDefinition);
        }
    }

    public List<GenericBeanDefinition> getScannerBeans() {
        return scannerBeans;
    }

    public void setScannerBeans(List<GenericBeanDefinition> scannerBeans) {
        this.scannerBeans = scannerBeans;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

}
