package com.zhanghe.util.excel.sheet.row.cell;

import com.zhanghe.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.util.ClassUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCellDateToProperty implements CellDataToPropertyConvert {
    @Override
    public Object convert(Cell cell, Class<?> c, PropertyAndColumn propertyAndColumn) {
        String stringValue = getStringValue(cell,propertyAndColumn);
        if(StringUtils.hasText(stringValue) && cell.getCellType() == CellType.NUMERIC){
            //匹配是否末尾有连串的0
                try {
                    double d = NumberUtils.parseNumber(stringValue,Double.class);
                    //是的转换为大的数据类型
                    BigDecimal bigDecimal =new BigDecimal(d);
                    stringValue = bigDecimal+"";
                } catch (Exception e) {
                    //不处理异常
                }
        }
        return stringValue;
    }

    private String getStringValue(Cell cell, PropertyAndColumn propertyAndColumn) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.FORMULA) {
            return cell.getCellFormula();
        } else if (cell.getCellType() == CellType.NUMERIC) {
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
        return ClassUtils.isAssignable(CharSequence.class,c);
    }
}
