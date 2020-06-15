package com.zhanghe.poi.util.excel.sheet.row.cell.property;

import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.util.ClassUtils;

/**
 * Number类型写入
 */
public class NumberCellConverter implements PropertyToCellDataConvert {
    @Override
    public boolean setConvert(Cell cell, Object val, PropertyAndColumn propertyAndColumn) {
        Number number = (Number) val;
        double v = number.doubleValue();
        cell.setCellValue(v);
        return true;
    }

    @Override
    public boolean canConvert(Class<?> c,PropertyAndColumn propertyAndColumn) {
        return ClassUtils.isAssignable(Number.class,propertyAndColumn.getType());
    }
}
