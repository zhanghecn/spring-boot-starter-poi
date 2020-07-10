package com.zhanghe.poi.util.excel.sheet.row.cell.property;

import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import com.zhanghe.poi.util.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 时间类型写入
 */
public class DateCellConvert implements PropertyToCellDataConvert {
    ConversionService conversionService = DefaultConversionService.getSharedInstance();
    public static final String DATE = "yyyy-MM-dd";
    public static final String DATE_TIME_2 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME = "HH:mm:ss";
    public static final String TIME_2 = "HH:mm:ss.SSSXXX";
    @Override
    public boolean setConvert(Cell cell, Object val, PropertyAndColumn propertyAndColumn) {
        Class<?> aClass = val.getClass();
        try {
            String dateFormat = propertyAndColumn.getDateFormat();
            //没有采用默认的时间格式
            if(!StringUtils.hasText(dateFormat)){
                dateFormat = DATE;
            }
            Date date =null;
            //是否是自定义的时间格式
            if(ClassUtils.isAssignable(CharSequence.class,aClass)){
                 date = DateUtils.stringToDate(val + "", dateFormat);
            }else if(ClassUtils.isAssignable(Date.class,aClass)){
                date = (Date) val;
            }
            cell.setCellValue(date);
            CellStyle cellStyle = handleDateFormat(cell, dateFormat);
            cell.setCellStyle(cellStyle);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public CellStyle handleDateFormat(Cell cell, String dateFormat) {
        CreationHelper creationHelper = cell.getSheet().getWorkbook().getCreationHelper();
        CellStyle cellStyle = cell.getCellStyle();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(dateFormat));
        return cellStyle;
    }

    @Override
    public boolean canConvert(Class<?> c, PropertyAndColumn propertyAndColumn) {
        //要么是时间类型的父类 要么给了格式化
        return ClassUtils.isAssignable( Date.class,c)||(StringUtils.hasText(propertyAndColumn.getDateFormat())&& ClassUtils.isAssignable(CharSequence.class,c));
    }
}
