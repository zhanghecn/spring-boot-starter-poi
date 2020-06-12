package com.zhanghe.util.excel.mapper;

import com.zhanghe.util.excel.sheet.SheetHandlerInfo;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.function.Predicate;

/**
 * Excel转换对象接口
 */
public interface ExcelObjectMapper {
     List getListObject(Workbook workbook, Predicate<Sheet> sheetPredicate);

    void getExcel(Workbook template, List<?> list);

    default List getListObject(Workbook workbook){
         return getListObject(workbook,null);
    }

     boolean comparison(Workbook template, SheetHandlerInfo sheetHandlerInfo1);
}
