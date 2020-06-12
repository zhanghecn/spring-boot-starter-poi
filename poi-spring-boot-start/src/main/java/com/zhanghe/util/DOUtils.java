package com.zhanghe.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 领域对象的工具
 */
public abstract class DOUtils {
    /**
     * json转换工具
     */
  private static ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

    /**
     * 一个集合类型转换另一种类型的集合
     * @param src
     * @param copy
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(Collection<?> src,Class<T> copy){
       Assert.isTrue(src!=null,"无法获取null的参数");
       List<T> list = new ArrayList<>(src.size());
       for (Object o : src) {
           T t = copyProperties(o, copy);
           list.add(t);
       }
       return list;
   }

    /**
     * 把对象copy到指定类型
     * @param o
     * @param copy
     * @param <T>
     * @return
     */
   public static <T>T copyProperties(Object o,Class<T> copy){
       T t = BeanUtils.instantiateClass(copy);
       BeanUtils.copyProperties(o,t);
       return t;
   }

    /**
     * 对象copy到对象
     * @param src
     * @param to
     */
    public static void copyObject(Object src,Object to){
        BeanUtils.copyProperties(src,to);
    }

    /**
     * copy 对象忽略空的属性
     * @param src
     * @param to
     */
    public static void copyObjectIgnoreNull(Object src,Object to){
        Class<?> targetPd = to.getClass();
        Set<String> ignoreProperties = getIgnoreNames(to, targetPd);
        String[] ignoreNames = ignoreProperties.toArray(new String[]{});
        BeanUtils.copyProperties(src,to,ignoreNames);
    }

    /**
     * 空字符串设置为null
     * @param to 对象是什么
     * @param targetPd 这个对象的类是什么
     */
    public  static void cleanEmptyString(Object to, Class<?> targetPd){
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(targetPd);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();
            Method writeMethod = propertyDescriptor.getWriteMethod();
            Class<?> returnType = readMethod.getReturnType();
            if (ClassUtils.isAssignable(CharSequence.class,returnType)) {
                try {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                        writeMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(to);
                    if(value!=null && !StringUtils.hasText(value+"")){
                        value= null;
                        writeMethod.invoke(to,value);
                    }
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
        }
    }

    /**
     * 清空空字符串 用做mybatis 忽略 null查询
     * @param to
     */
    public  static void cleanEmptyString(Object to){
        cleanEmptyString(to,to.getClass());
    }

    /**
     * 获取空字符串字段
     * @param to
     * @param targetPd
     * @return
     */
    public static Set<String> getIgnoreNames(Object to, Class<?> targetPd) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(targetPd);
        Set<String> ignoreProperties = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                try {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(to);
                   if(value!=null && !Objects.equals(value,0)&& StringUtils.hasText(value+"")){
                       ignoreProperties.add(propertyDescriptor.getName());
                   }
                }
                catch (Throwable ex) {
                    throw new FatalBeanException(
                            "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                }
            }
        }
        return ignoreProperties;
    }

    /**
     * 对象到map
     * @param src
     * @return
     */
    public static Map<String,Object> toMap(Object src)
    {
        Assert.isTrue(objectMapper.canSerialize(src.getClass()),src.getClass()+"没有序列化。");
        try {
            //先转换json
            String s = objectMapper.writeValueAsString(src);
            //在转换Map
            return objectMapper.readValue(s, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * map到对象
     * @param
     * @return
     */
    public static <T> T toObject(Map<String,Object> map, Class<T> c)
    {
        Assert.isTrue(objectMapper.canSerialize(c),c+"没有序列化。");
        try {
            //先转换json
            String s = objectMapper.writeValueAsString(map);
            //在转换Map
            return objectMapper.readValue(s, c);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
