package com.zhanghe.poi.util.excel.sheet;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @Author: ZhangHe
 * @Date: 2020/5/29 8:51
 */
public abstract class AbstractSheetHandler extends AbstractSheetInfoType implements SheetHandler{
    public AbstractSheetHandler(SheetHandlerInfo sheetInfo) {
        super(sheetInfo);
    }

    protected XSSFSheet xssfSheet(){
        XSSFWorkbook sheets = xssfWorkbook();
        return sheets.getSheet(this.sheetInfo.getSheet().getSheetName());
    }

    protected XSSFWorkbook xssfWorkbook(){
        Workbook workbook = this.sheetInfo.getSheet().getWorkbook();
        if(workbook instanceof SXSSFWorkbook){
            SXSSFWorkbook sxssfWorkbook = (SXSSFWorkbook) workbook;
            XSSFWorkbook xssfWorkbook = sxssfWorkbook.getXSSFWorkbook();
            return xssfWorkbook;
        }
        return (XSSFWorkbook) workbook;
    }
}
