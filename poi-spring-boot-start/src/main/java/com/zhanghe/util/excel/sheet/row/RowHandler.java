package com.zhanghe.util.excel.sheet.row;

import com.zhanghe.util.excel.type.ExcelTypeWrap;
import org.apache.poi.ss.usermodel.Row;

/**
 * @Author: ZhangHe
 * @Date: 2020/4/21 17:15
 */
public interface RowHandler {
    /**
     * 获取该行对象
     * @return
     */
    Object getObject();


    /**
     * 写入该行数据
     * @param o
     */
     void writeRow(Object o);

    /**
     * 处理该行数据
     * @param o
     * @param row
     * @param excelTypeWraps
     */
     void handlerCells(Object o, Row row, ExcelTypeWrap excelTypeWraps);


}
