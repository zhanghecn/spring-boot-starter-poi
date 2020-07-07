package com.zhanghe.util.excel.sheet.row.cell.property;

import com.zhanghe.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.ClassUtils;

public class BooleanCellConvert implements PropertyToCellDataConvert {
    ConversionService conversionService = DefaultConversionService.getSharedInstance();
    @Override
    public boolean setConvert(Cell cell, Object val, PropertyAndColumn propertyAndColumn) {
        try {
            Boolean convert = conversionService.convert(val, Boolean.class);
            if(convert==null){
                return false;
            }
            cell.setCellValue(convert);
        } catch (Exception e) {
          return false;
        }
        return true;
    }

    @Override
    public boolean canConvert(Class<?> c, PropertyAndColumn propertyAndColumn) {
        return ClassUtils.isAssignable(boolean.class,c);
    }
}
