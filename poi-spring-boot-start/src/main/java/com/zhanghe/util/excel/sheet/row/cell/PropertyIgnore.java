package com.zhanghe.util.excel.sheet.row.cell;

import com.zhanghe.autoconfig.annotation.style.ColumnStyle;
import com.zhanghe.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author: ZhangHe
 * @since: 2020/4/24 16:49
 */

/**
 * 属性忽略 对指定类型的字段属性不进行任何操作
 */
public class PropertyIgnore implements CellDataToPropertyConvert {
    static final Map<Class,Boolean> ignoreClasses;

    static {
        ignoreClasses = new HashMap<>();
        ignoreClasses.put(ColumnStyle.class,true);
    }

    @Override
    public Object convert(Cell cell, Class<?> c, PropertyAndColumn propertyAndColumn) {
        return null;
    }

    @Override
    public boolean canConvert(Class<?> c) {
        return Optional.ofNullable(ignoreClasses.get(c)).orElseGet(()->false);
    }
}
