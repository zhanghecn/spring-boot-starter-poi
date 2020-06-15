package com.zhanghe.poi.util;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpringContextHelper implements ApplicationContextAware, ResourceLoaderAware {
    public static ApplicationContext applicationContext;

    public static ResourceLoader resourceLoader;

    public static <T>List<T> getBeans(Class<T> tClass) {
        String[] beanNamesForType = SpringContextHelper.applicationContext.getBeanNamesForType(tClass);
        if (!ObjectUtils.isEmpty(beanNamesForType)) {
            List<Object> collect = Stream.of(beanNamesForType).map(beanName -> SpringContextHelper.applicationContext.getBean(beanName)).collect(Collectors.toList());
            return (List<T>) collect;
        }
        return Collections.EMPTY_LIST;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHelper.applicationContext = applicationContext;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        SpringContextHelper.resourceLoader = resourceLoader;
    }
}
