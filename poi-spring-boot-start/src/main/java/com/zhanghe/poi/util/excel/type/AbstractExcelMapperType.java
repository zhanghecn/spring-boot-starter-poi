package com.zhanghe.poi.util.excel.type;


import com.zhanghe.poi.util.MapUtils;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel 和 类型 对应关系
 * 抽象出拥有的字段和方法
 * @Author: ZhangHe
 * @Date: 2020/4/22 17:15
 */
@Data
public abstract  class AbstractExcelMapperType {
    /**
     * excel所对应的类型
     */
    protected    Class<?> aClass;
    //具有详细分配的属性和excel列的关系 也就是实体类属性不是map的
    protected Map<String,PropertyAndColumn> detailProAndColumns;

    //与detailProAndColumns相反
    protected Map<String,PropertyAndColumn> noDetailProAndColumns;
    //具体会被操作的属性列
    protected List<PropertyAndColumn> propertyAndColumns;
    //特殊map属性处理 处理方式:其余的列全部放在map属性里面
    protected List<PropertyAndColumn> mapPropertyAndColumns;

    protected PropertyDescriptor[] propertyDescriptors;



    protected void formatPropertyAndColumns(Map<String,Integer> headers) {
        //是map的话就不用做映射关系了
        if(ClassUtils.isAssignable(Map.class,aClass)){
            return;
        }
        initContainer();
        Map<Integer, String> indexes = MapUtils.valueKeyReverse(headers);
        //扩展头部
        if(indexes!=null){
            extendHeaders(indexes);
        }
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            //获取属性信息
            Class<?> propertyType = propertyDescriptor.getPropertyType();
            String name = propertyDescriptor.getName();
            Method readMethod = propertyDescriptor.getReadMethod();
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if(readMethod!=null&&writeMethod!=null){
                Field field = ReflectionUtils.findField(aClass, name);
                if(field==null){
                    continue;
                }
                handField(field,headers, indexes, propertyType, name,propertyDescriptor);
            }
        }
        //处理map属性
        handlerNoDetailProAndColumns(indexes);
    }

    /**
     * 给父类处理字段
     * @param field
     * @param headers
     * @param indexes
     * @param propertyType
     * @param name
     * @param propertyDescriptor
     */
    protected abstract void handField(Field field, Map<String, Integer> headers, Map<Integer, String> indexes, Class<?> propertyType, String name, PropertyDescriptor propertyDescriptor);


    /**
     * 初始化容器
     */
    protected void initContainer() {
        this.detailProAndColumns = new HashMap<>();
        this.noDetailProAndColumns = new HashMap<>();
        this.mapPropertyAndColumns = new ArrayList<>(3);
        this.propertyDescriptors = BeanUtils.getPropertyDescriptors(aClass);
        this.propertyAndColumns = new ArrayList<>(propertyDescriptors.length-1);
    }

    /**
     * 处理map属性
     * @param indexes
     */
    protected abstract void handlerNoDetailProAndColumns(Map<Integer, String> indexes);

    /**
     * 扩展头部
     * @param headers
     */
    protected abstract void extendHeaders(Map<Integer, String> headers);
}
