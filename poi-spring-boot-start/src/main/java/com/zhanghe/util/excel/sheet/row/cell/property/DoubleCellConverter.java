package com.zhanghe.util.excel.sheet.row.cell.property;

import com.zhanghe.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * double类型写入
 */
public class DoubleCellConverter implements PropertyToCellDataConvert {
    ConversionService conversionService = DefaultConversionService.getSharedInstance();
    @Override
    public boolean setConvert(Cell cell, Object val, PropertyAndColumn propertyAndColumn) {
        try {
            Double convert = conversionService.convert(val, Double.class);
            String s = convert + "";
            Matcher mer = Pattern.compile("^[+-]?[0-9]+(.[0-9]+){1}$").matcher(s);
            //如果没有转换成功，并且不是常用的double数
            if(convert==null||!mer.find()){
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
    public boolean canConvert(Class<?> c,PropertyAndColumn propertyAndColumn) {
        return c!=Long.class&&c!=long.class&&conversionService.canConvert(c,Double.class);
    }
}
