package com.zhanghe.util.excel.sheet.row.cell.property;

import com.zhanghe.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.Calendar;

/**
 * 日期类型写入
 */
public class CalendarCellConverter implements PropertyToCellDataConvert {
    ConversionService conversionService = DefaultConversionService.getSharedInstance();
    @Override
    public boolean setConvert(Cell cell, Object val,  PropertyAndColumn propertyAndColumn) {
        try {
            Calendar convert = conversionService.convert(val, Calendar.class);
            if(convert==null){
                return false;
            }
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(convert);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canConvert(Class<?> c, PropertyAndColumn propertyAndColumn) {
        return conversionService.canConvert(c, Calendar.class);
    }
}
