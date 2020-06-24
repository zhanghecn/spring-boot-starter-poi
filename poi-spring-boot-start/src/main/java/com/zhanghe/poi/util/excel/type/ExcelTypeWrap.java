package com.zhanghe.poi.util.excel.type;


import com.zhanghe.poi.autoconfig.annotation.ExcelColumn;
import com.zhanghe.poi.autoconfig.annotation.ExcelRow;
import com.zhanghe.poi.autoconfig.annotation.type.OutType;
import com.zhanghe.poi.util.DOUtils;
import com.zhanghe.poi.util.excel.sheet.row.cell.PropertyIgnore;
import lombok.Data;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * excel映射类型封装-中间映射类
 */
@Data
public class ExcelTypeWrap  extends AbstractExcelMapperType {

    private  static  ThreadLocal<Map<Integer, String>> extendValues = new ThreadLocal<>();

    //忽略的类型属性
    private static PropertyIgnore propertyIgnore = new PropertyIgnore();

    private static Map<Class<?>,ExcelTypeWrap> cacheExcelTypeWraps = new ConcurrentHashMap<>();

    private ExcelTypeWrap(Class<?> aClass,Map<String,Integer> headers) {
        this.aClass = aClass;
        formatPropertyAndColumns(headers);
    }

    private ExcelTypeWrap(Class<?> aClass) {
        this.aClass = aClass;
        formatPropertyAndColumns(null);
    }

    public static void setCurrentValue(Map<Integer,String> t){
        extendValues.set(t);
    }

    public   static  Map<Integer,String> getCurrentValue(){
        return extendValues.get();
    }

    public static void cleanCurrentValue(){
         extendValues.remove();
    }
    /**
     * 添加扩展头部
     * @param header 头部名称
     * @param index  下标
     */
    public static void putExtendHeader(String header,int index){
        Map<Integer,String> currentValue = getCurrentValue();
        if(currentValue==null){
            currentValue = new HashMap<>();
            setCurrentValue(currentValue);
        }
        currentValue.put(index,header);
    }
    //缓存获取
    public static ExcelTypeWrap getExcelTypeWrap(Class<?> aClass,Map<String,Integer> headers){
        ExcelTypeWrap excelTypeWrap = cacheExcelTypeWraps.get(aClass);
        if(excelTypeWrap==null){
            excelTypeWrap = new ExcelTypeWrap(aClass,headers);
            cacheExcelTypeWraps.put(aClass,excelTypeWrap);
        }
        return excelTypeWrap;
    }
    public static ExcelTypeWrap getExcelTypeWrap(Class<?> aClass){
        return getExcelTypeWrap(aClass,null);
    }


    /**
     * 扩展头部标题信息
     * @param headers
     */
    protected void extendHeaders(Map<Integer, String> headers) {
        Map<Integer, String> currentValue = getCurrentValue();
        if(currentValue!=null){
            headers.putAll(currentValue);
        }
    }

    /**
     * 通过属性名称和列注解 属性类型封装PropertyAndColumn
     * @param propertyType
     * @param name
     * @param mergedAnnotation
     * @param propertyDescriptor
     * @return
     */
    public PropertyAndColumn getComponentColumn(Class<?> propertyType, String name, ExcelColumn mergedAnnotation, PropertyDescriptor propertyDescriptor){
        int value = mergedAnnotation.value();
        String s = mergedAnnotation.dateFormat();
        OutType outType = mergedAnnotation.outType();
        //赋值
        PropertyAndColumn propertyAndColumn = new PropertyAndColumn();
        propertyAndColumn.setColumnIndex(value);
        propertyAndColumn.setProperty(name);
        propertyAndColumn.setType(propertyType);
        propertyAndColumn.setDateFormat(s);
        propertyAndColumn.setOutType(outType);
        propertyAndColumn.setExcelTypeWrap(this);
        propertyAndColumn.setPropertyDescriptor(propertyDescriptor);
        return propertyAndColumn;
    }

    /**
     * 封装基本PropertyAndColumn基础上对应头部标题
     * @param headers
     * @param indexes
     * @param propertyType
     * @param name
     * @param mergedAnnotation
     * @param propertyDescriptor
     * @return
     */
    public PropertyAndColumn getPropertyAndColumn(Map<String, Integer> headers,Map<Integer, String> indexes, Class<?> propertyType, String name, ExcelColumn mergedAnnotation,PropertyDescriptor propertyDescriptor) {
        PropertyAndColumn propertyAndColumn = getComponentColumn(propertyType, name, mergedAnnotation, propertyDescriptor);
        String columnName = mergedAnnotation.columnName();
        if (headers!=null) {
            if (StringUtils.hasText(columnName)) {
                Integer integer = headers.get(columnName);
                Assert.isTrue(integer != null, "与(" + columnName+")头不对应");
                propertyAndColumn.setColumnIndex(integer);
            }else{
                //获取头部名称
                String headerName = indexes.get(propertyAndColumn.getColumnIndex());
                propertyAndColumn.setColumnName(headerName);
            }
        }
        return propertyAndColumn;
    }

    /**
     * 把列信息储存起来
     * @param values
     */
    protected void addHandlerMapProperty(Collection<PropertyAndColumn> values) {
        this.propertyAndColumns.addAll(values);
    }


    @Override
    protected void handField(Field field, Map<String, Integer> headers, Map<Integer, String> indexes, Class<?> propertyType, String name, PropertyDescriptor propertyDescriptor) {
        ExcelColumn mergedAnnotation = AnnotatedElementUtils.getMergedAnnotation(field, ExcelColumn.class);
        ExcelRow excelRow = AnnotatedElementUtils.getMergedAnnotation(field, ExcelRow.class);
        Map<String, Object> rowDetails = null;
        if(excelRow!=null){
             rowDetails = AnnotationUtils.getAnnotationAttributes(excelRow);
        }
        //没有注解的话
        if(mergedAnnotation==null){
            //跳过
            return;
        }
        PropertyAndColumn propertyAndColumn = getPropertyAndColumn(headers, indexes, propertyType, name, mergedAnnotation,propertyDescriptor);
        //设置详情属性
        Map<String, Object> columnDetails = AnnotationUtils.getAnnotationAttributes(mergedAnnotation);

        propertyAndColumn.setExcelColumn(columnDetails);
        propertyAndColumn.setExcelRow(rowDetails);

        //不是map
        if(!ClassUtils.isAssignable(Map.class,propertyType)){
            propertyAndColumns.add(propertyAndColumn);
            if(indexes!=null) {
                //获取头部
                String header = indexes.get(propertyAndColumn.getColumnIndex());
                //给与头部
                detailProAndColumns.put(header, propertyAndColumn);
            }
        }else{
            //map属性
            mapPropertyAndColumns.add(propertyAndColumn);
        }
    }

    /**
     * 把对象中的map字段 去 和 头部标题信息 转换为PropertyAndColumn 添加
     * @param indexes
     */
    protected void handlerNoDetailProAndColumns( Map<Integer, String> indexes) {
        if(indexes==null||mapPropertyAndColumns.isEmpty()){
            return;
        }
        List<PropertyAndColumn> propertyAndColumns = new ArrayList<>(mapPropertyAndColumns.size()*indexes.size());
        for (PropertyAndColumn mapPropertyAndColumn : mapPropertyAndColumns) {
            //遍历所有头部
            for (Map.Entry<Integer, String> stringIntegerEntry : indexes.entrySet()) {
                PropertyAndColumn propertyAndColumn = DOUtils.copyProperties(mapPropertyAndColumn, PropertyAndColumn.class);
                Integer key = stringIntegerEntry.getKey();
                String value = stringIntegerEntry.getValue();
                //已经被使用了就跳过但不包括忽略的属性 忽略的属性一般是样式列等，这种值还是要放进map的
                if(detailProAndColumns.get(value)!=null&&!propertyIgnore.canConvert(detailProAndColumns.get(value).getType())){
                    continue;
                }
                propertyAndColumn.setColumnName(value);
                propertyAndColumn.setColumnIndex(key);
                propertyAndColumns.add(propertyAndColumn);
                noDetailProAndColumns.put(key+"",propertyAndColumn);
            }
        }
        //添加map属性的列
        addHandlerMapProperty(propertyAndColumns);
    }




}
