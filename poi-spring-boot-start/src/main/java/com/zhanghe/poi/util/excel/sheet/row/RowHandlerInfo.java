package com.zhanghe.poi.util.excel.sheet.row;


import com.zhanghe.poi.util.excel.sheet.SheetHandlerInfo;
import com.zhanghe.poi.util.excel.type.ExcelTypeWrap;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;

import java.io.Serializable;

/**
 * 操作行的信息
 */
@Data
public class RowHandlerInfo implements Serializable {
    //该行映射的类型信息
   private ExcelTypeWrap excelTypeWraps;
   //要操作的行
   private Row row;
   //所属sheet页的信息
    private SheetHandlerInfo sheetHandlerInfo;

    public RowHandlerInfo(ExcelTypeWrap excelTypeWraps, Row row) {
        this.excelTypeWraps = excelTypeWraps;
        this.row = row;
    }
}
