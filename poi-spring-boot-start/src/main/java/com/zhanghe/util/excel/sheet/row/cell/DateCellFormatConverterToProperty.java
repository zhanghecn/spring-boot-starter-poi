package com.zhanghe.util.excel.sheet.row.cell;

import com.zhanghe.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.util.ClassUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class DateCellFormatConverterToProperty implements CellDataToPropertyConvert {
    @Override
    public Object convert(Cell cell, Class<?> c, PropertyAndColumn propertyAndColumn) {
        if(cell.getCellType()== CellType.NUMERIC){
            return cell.getDateCellValue();
        }
        try {
            return new DateFormatter().parse(cell.toString(), Locale.CHINESE);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public boolean canConvert(Class<?> c) {
        return ClassUtils.isAssignable(Date.class,c);
    }
}
