package com.zhanghe.util.excel.sheet.row.cell;

import com.zhanghe.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.BeanWrapper;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class MapPropertyConverter implements CellDataToPropertyConvert {
    CellDataToPropertyConvert cellDataToPropertyConvert = CommonCellConverterToProperty.instantiation();
    private BeanWrapper beanWrapper;

    public MapPropertyConverter(BeanWrapper beanWrapper) {
        this.beanWrapper = beanWrapper;
    }

    @Override
    public Object convert(Cell cell, Class<?> c, PropertyAndColumn propertyAndColumn) {
        //获取字段的类型解析
        ResolvableType resolvableType = ResolvableType.forField(Objects.requireNonNull(ReflectionUtils.findField(propertyAndColumn.getExcelTypeWrap().getAClass(), propertyAndColumn.getProperty())));
        //获取第一个泛型解析类型
        ResolvableType generic = resolvableType.getGeneric(0);
        Type type = generic.getType();
        Map propertyValue = (Map<String, Object>) beanWrapper.getPropertyValue(propertyAndColumn.getProperty());
        //如果属性没有初始化
        if (propertyValue == null) {
            propertyValue = new LinkedHashMap();
            beanWrapper.setPropertyValue(propertyAndColumn.getProperty(), propertyValue);
        }
        if(type==int.class||type==Integer.class){
            //下标为key
            Object convert = cellDataToPropertyConvert.convert(cell, String.class, propertyAndColumn);
            propertyValue.put(propertyAndColumn.getColumnIndex(), convert);
        }else {
            //头部名称为key
            Object convert = cellDataToPropertyConvert.convert(cell, String.class, propertyAndColumn);
            propertyValue.put(propertyAndColumn.getColumnName(), convert);
        }
        return propertyValue;
    }

    @Override
    public boolean canConvert(Class<?> c) {
        return ClassUtils.isAssignable(Map.class,c);
    }
}
