package com.zhanghe.poi.util.excel.sheet.row.cell.property;

import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;

public interface PropertyToCellDataConvert {
    /**
     * 属性值转换cell可以赋值的类型
     * @param val 属性值
     * @param propertyAndColumn 属性和列的关系
     * @return
     */
    boolean setConvert(Cell cell, Object val, PropertyAndColumn propertyAndColumn);

    /**
     * 能否转换此类型
     * @param c
     * @return
     */
    boolean canConvert(Class<?> c, PropertyAndColumn propertyAndColumn);
}
