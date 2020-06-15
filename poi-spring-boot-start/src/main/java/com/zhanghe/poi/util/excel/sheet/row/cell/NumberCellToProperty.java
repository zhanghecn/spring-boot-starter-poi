package com.zhanghe.poi.util.excel.sheet.row.cell;

import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.util.ClassUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数字类型的列到数字类型的属性
 */
public class NumberCellToProperty implements CellDataToPropertyConvert {
    @Override
    public Object convert(Cell cell, Class<?> c, PropertyAndColumn propertyAndColumn) {
        String stringValue = getStringValue(cell,propertyAndColumn);
        if(stringValue!=null){
            Object d = NumberUtils.parseNumber(stringValue,(Class<? extends Number>) c);
            return d;
        }
        return null;
    }

    private String getStringValue(Cell cell, PropertyAndColumn propertyAndColumn) {
       if (cell.getCellType() == CellType.NUMERIC) {
            if(propertyAndColumn!=null&& StringUtils.hasText(propertyAndColumn.getDateFormat())){
                Date dateCellValue = cell.getDateCellValue();
                return new SimpleDateFormat(propertyAndColumn.getDateFormat()).format(dateCellValue);
            }
            return String.valueOf(cell.getNumericCellValue());
        }
        return null;
    }

    @Override
    public boolean canConvert(Class<?> c) {
        return ClassUtils.isAssignable(Number.class,c);
    }
}
