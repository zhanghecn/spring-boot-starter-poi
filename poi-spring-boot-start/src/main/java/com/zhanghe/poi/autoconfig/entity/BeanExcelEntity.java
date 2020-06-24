package com.zhanghe.poi.autoconfig.entity;


import com.zhanghe.poi.autoconfig.annotation.ExcelGroupEntity;
import com.zhanghe.poi.util.DOUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean => ExcelEntity
 * @Author: ZhangHe
 * @Date: 2020/5/27 15:02
 */
public class BeanExcelEntity extends ExcelEntity implements Serializable {
    //TODO:这个只有新版本Spring有
//    private static BeanNameGenerator beanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;
    //采用旧版本
    private static BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();;
    protected BeanExcelEntity() {
        super(ExcelEntity.builder());
    }

    /**
     * 从ExcelGroupEntity bean定义集合转换成  Map<String, List<ExcelEntity>>
     * @param beanDefinitions
     * @return
     */
    public static Map<String, List<ExcelEntity>> getExcelEntities(List<GenericBeanDefinition> beanDefinitions) throws ClassNotFoundException {
        Map<String, List<ExcelEntity>> excelEntities = new HashMap<>();
        List<GenericBeanDefinition> scannerBeans = beanDefinitions;
        for (GenericBeanDefinition beanDefinition : scannerBeans) {
            if(beanDefinition instanceof AnnotatedBeanDefinition){
                AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
                String beanClassName = annotatedBeanDefinition.getBeanClassName();
                Class<?> aClass = ClassUtils.forName(beanClassName, ClassUtils.getDefaultClassLoader());
                AnnotationMetadata metadata = annotatedBeanDefinition.getMetadata();
                //获取注解值
                Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ExcelGroupEntity.class.getCanonicalName());
                //获取分组id
                String groupId = (String) annotationAttributes.get("groupId");

                ExcelEntity excelEntity = DOUtils.toObject(annotationAttributes, BeanExcelEntity.class);

                if(StringUtils.isEmpty(groupId)){
                    groupId = beanNameGenerator.generateBeanName(beanDefinition, null);
                    excelEntity.setGroupId(groupId);
                }
                //设置映射的类
                excelEntity.setClazz(aClass);
                //添加excel
                List<ExcelEntity> orDefault = excelEntities.getOrDefault(groupId, new ArrayList<>());
                orDefault.add(excelEntity);
                excelEntities.put(groupId,orDefault);
            }
        }
        return excelEntities;
    }
}
