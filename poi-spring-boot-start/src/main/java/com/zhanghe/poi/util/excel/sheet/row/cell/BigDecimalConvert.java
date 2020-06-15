package com.zhanghe.poi.util.excel.sheet.row.cell;

import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.math.BigDecimal;

/**
 * @Author: Yin BenLi
 * @Description: 大数据转换类型
 * @Date: Create in 8:43 2020/5/27
 */
public class BigDecimalConvert implements CellDataToPropertyConvert {
    @Override
    public Object convert(Cell cell, Class<?> c, PropertyAndColumn propertyAndColumn) {
        if (cell.getCellType() == CellType.NUMERIC) {
            cell.setCellType(CellType.STRING);//先重置单元格格式
            return String.valueOf(new BigDecimal(cell.getStringCellValue()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());//再 直接截取两位【枚举可自由替换】
        }

        return null;
    }

    @Override
    public boolean canConvert(Class<?> c) {
        return c== BigDecimal.class;
    }
}
