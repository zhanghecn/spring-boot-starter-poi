package com.zhanghe.util.excel.sheet.row.cell.property;

import com.zhanghe.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;

public interface PropertyToCellDataConvert {

    boolean setConvert(Cell cell, Object val, PropertyAndColumn propertyAndColumn);

    boolean canConvert(Class<?> c, PropertyAndColumn propertyAndColumn);
}
