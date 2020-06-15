package com.zhanghe.poi.util.excel.sheet.row.cell.property;

import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class StringCellConvert implements PropertyToCellDataConvert {
    @Override
    public boolean setConvert(Cell cell, Object val,  PropertyAndColumn propertyAndColumn) {
        cell.setCellType(CellType.STRING);
        cell.setCellValue(val+"");
        return true;
    }

    @Override
    public boolean canConvert(Class<?> c, PropertyAndColumn propertyAndColumn) {
        return true;
    }
}
