package com.zhanghe.util.excel.sheet;

import com.zhanghe.util.excel.type.ExcelTypeWrap;
import lombok.Data;

import java.util.Map;

/**
 * @author: ZhangHe
 * @since: 2020/5/26 15:08
 */
@Data
public abstract class AbstractSheetInfoType {
    protected SheetHandlerInfo sheetInfo;
    protected ExcelTypeWrap excelTypeWrap;
    protected Map<String, Integer> headers;

    public AbstractSheetInfoType(SheetHandlerInfo sheetInfo) {
        this.sheetInfo = sheetInfo;
        this.headers = SheetHandler.getSheetHeaders(this.sheetInfo);
        this.excelTypeWrap = ExcelTypeWrap.getExcelTypeWrap(sheetInfo.getTClass(),headers);
    }
}
