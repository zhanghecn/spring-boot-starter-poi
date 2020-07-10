package com.zhanghe.poi.util.excel.sheet.row.cell;

import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;

public interface CellDataToPropertyConvert {

     Object convert(Cell cell, Class<?> c, PropertyAndColumn propertyAndColumn);

    /**
     * 能否转换此类型
     * @param c 类型
     * @return 能转换这个类型
     */
    boolean canConvert(Class<?> c);

    /**
     * 可能转换的类型
     * @param propertyAndColumn 属性和列对应信息
     * @return 是否可以转换这个列
     */
    default   boolean isConvertColumn(PropertyAndColumn propertyAndColumn){
        return false;
    }
}
