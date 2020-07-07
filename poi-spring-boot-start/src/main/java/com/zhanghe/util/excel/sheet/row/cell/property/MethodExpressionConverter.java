package com.zhanghe.util.excel.sheet.row.cell.property;

import com.zhanghe.util.evaluation.MethodEvaluation;
import com.zhanghe.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;

/**
 * 兼容新版本的方法，但又要兼容旧版本的。所以采用表达式设置值
 * @author: ZhangHe
 * @since: 2020/7/7 11:35
 */
public class MethodExpressionConverter implements PropertyToCellDataConvert{

    @Override
    public boolean setConvert(Cell cell, Object val, PropertyAndColumn propertyAndColumn) {
        MethodEvaluation.invoke(cell,"setCellValue(#value)",val);
        return true;
    }

    @Override
    public boolean canConvert(Class<?> c, PropertyAndColumn propertyAndColumn) {
        return true;
    }
}
