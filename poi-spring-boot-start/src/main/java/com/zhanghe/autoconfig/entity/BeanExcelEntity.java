package com.zhanghe.autoconfig.entity;


import com.zhanghe.autoconfig.annotation.ExcelGroupEntity;
import com.zhanghe.util.DOUtils;
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
    private static BeanNameGenerator beanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;

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
