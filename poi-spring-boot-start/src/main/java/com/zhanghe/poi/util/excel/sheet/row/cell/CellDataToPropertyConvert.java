package com.zhanghe.poi.util.excel.sheet.row.cell;

import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;

public interface CellDataToPropertyConvert {
    /**
     * 一个Cell数据转换器
     * @param cell  要转换的单元格
     * @param c 转换为什么类型
     * @return
     */
     Object convert(Cell cell, Class<?> c, PropertyAndColumn propertyAndColumn);

    /**
     * 能否转换此类型
     * @param c
     * @return
     */
    boolean canConvert(Class<?> c);

    /**
     * 可能转换的类型
     * @param propertyAndColumn
     * @return
     */
    default   boolean isConvertColumn(PropertyAndColumn propertyAndColumn){
        return false;
    }
}
