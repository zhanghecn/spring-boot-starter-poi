package com.zhanghe.poi.util.excel.sheet.row.cell.property;

import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.util.ClassUtils;

public class StringCellConvert implements PropertyToCellDataConvert {
    @Override
    public boolean setConvert(Cell cell, Object val,  PropertyAndColumn propertyAndColumn) {
        cell.setCellValue(String.valueOf(val));
        return true;
    }

    @Override
    public boolean canConvert(Class<?> c, PropertyAndColumn propertyAndColumn) {
        return ClassUtils.isAssignable(CharSequence.class,c);
    }
}
