package com.zhanghe.util.excel.sheet.row.cell.property;

import com.zhanghe.util.excel.type.ExcelTypeWrap;
import com.zhanghe.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 设置扩展map的属性
 */
public class MapPropertyToCells implements PropertyToCellDataConvert {
    CommonSetCell commonSetCell = CommonSetCell.instantiation();

    /**
     * 设置值，并且给与样式
     * @param cell 列
     * @param val 值
     * @param propertyAndColumn 属性和列
     * @param cellStyle 样式
     * @return 是否设置好了样式
     */
    public boolean setAndStyle(Cell cell, Object val, PropertyAndColumn propertyAndColumn, CellStyle cellStyle) {
        commonSetCell.setStyle(cell,cellStyle);
        return setConvert(cell,val,propertyAndColumn);
    }
    @Override
    public boolean setConvert(Cell cell, Object val,PropertyAndColumn propertyAndColumn) {
        if(val==null){
            return false;
        }
        ExcelTypeWrap excelTypeWrap = propertyAndColumn.getExcelTypeWrap();
        Class<?> aClass = excelTypeWrap.getAClass();
        ResolvableType resolvableType = ResolvableType.forField(ReflectionUtils.findField(aClass, propertyAndColumn.getProperty()));
        Type type = resolvableType.getGeneric(0).getType();
        Map map;
        Object setObject;
        if(type==int.class||type==Integer.class){
            map = (Map<Integer, Object>) val;
            //根据下标取
            setObject = map.get(propertyAndColumn.getColumnIndex());
        }else{
            map = (Map<String, Object>) val;
            //根据名称取
            setObject = map.get(propertyAndColumn.getColumnName());
        }
        if(setObject!=null&& StringUtils.hasText(setObject+"")){
            commonSetCell.setConvert(cell, setObject, propertyAndColumn);
        }
        return true;
    }

    @Override
    public boolean canConvert(Class<?> c, PropertyAndColumn propertyAndColumn) {
        return ClassUtils.isAssignable(Map.class,c);
    }
}
