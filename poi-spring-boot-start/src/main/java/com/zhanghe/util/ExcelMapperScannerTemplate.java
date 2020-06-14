package com.zhanghe.util;

import com.zhanghe.autoconfig.entity.ExcelEntity;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExcelMapperScannerTemplate implements ExcelMapperUtil, BeanFactoryAware {
    private String templateLocation;
    private ListableBeanFactory beanFactory;
    private  Map<String, ExcelEntity> excelEntityMap = new ConcurrentHashMap<>();
    private  Map<String, List<ExcelEntity>> excelEntitiesMap = new ConcurrentHashMap<>();
    private Lock lock = new ReentrantLock();

    //TODO:这个只有新版本Spring有
//    private static BeanNameGenerator beanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;
    AnnotationBeanNameGenerator beanNameGenerator  = new AnnotationBeanNameGenerator();;

    public final static String BEAN_NAME = "excelMapperScannerTemplate";

    public ExcelMapperScannerTemplate(String templateLocation) {
        this.templateLocation = templateLocation;
    }

    public String getTemplateLocation() {
        return templateLocation;
    }

    public Map<String, List<ExcelEntity>> getExcelEntitiesMap() {
        return excelEntitiesMap;
    }

    public void setExcelEntitiesMap(Map<String, List<ExcelEntity>> excelEntitiesMap) {
        this.excelEntitiesMap = excelEntitiesMap;
    }

    @Override
    public ExcelEntity load(String configName) {
        lock.lock();
        ExcelEntity excelEntity;
        try {
            excelEntity = excelEntityMap.get(configName);
            if(excelEntity==null){
                load();
                excelEntity = excelEntityMap.get(configName);
            }
        } finally {
            lock.unlock();
        }
        return excelEntity;
    }

    @Override
    public List<ExcelEntity> loadGroups(String groupId) {
        List<ExcelEntity> es = new ArrayList<>();
        List<ExcelEntity> excelEntities = excelEntitiesMap.get(groupId);
        if(excelEntities==null||excelEntities.isEmpty()){
            ExcelEntity load = load(groupId);
            if(load!=null){
                es.add(load);
            }
        }else{
            es.addAll(excelEntities);
        }
        return es;
    }

    @Override
    public List<ExcelEntity> loadGroups(Class<?> c) {
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(c).getBeanDefinition();
        String beanName = beanNameGenerator.generateBeanName(beanDefinition, null);
        return loadGroups(beanName);
    }


    public void load(){
        String[] beanNamesForType = this.beanFactory.getBeanNamesForType(ExcelEntity.class);
        for (String name : beanNamesForType) {
            ExcelEntity bean = (ExcelEntity) this.beanFactory.getBean(name);
            if(!StringUtils.hasText(bean.getTemplateLocation())) {
                bean.setTemplateLocation(this.templateLocation);
            }
            excelEntityMap.put(name,bean);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }
}
