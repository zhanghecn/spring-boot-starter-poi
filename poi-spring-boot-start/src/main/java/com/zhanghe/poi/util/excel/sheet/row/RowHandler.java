package com.zhanghe.poi.util.excel.sheet.row;

import com.zhanghe.poi.util.excel.type.ExcelTypeWrap;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author: ZhangHe
 * @since: 2020/4/21 17:15
 */
public interface RowHandler {
    /**
     * 获取该行对象
     * @return 获取到的对象
     */
    Object getObject();


    /**
     * 写入该行数据
     * @param o 写入的对象
     */
     void writeRow(Object o);

    /**
     * 处理该行数据
     * @param o 写入的对象
     * @param row 写入的行
     * @param excelTypeWraps 类型封装信息
     */
     void handlerCells(Object o, Row row, ExcelTypeWrap excelTypeWraps);


}
