package com.zhanghe.util.excel.sheet;


import com.zhanghe.util.excel.sheet.row.RowHandler;
import com.zhanghe.util.excel.sheet.row.cell.CellDataToPropertyConvert;
import com.zhanghe.util.excel.sheet.row.cell.StringCellDateToProperty;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Sheet页处理
 * @Author: ZhangHe
 * @Date: 2020/4/22 16:57
 */
public interface SheetHandler {
    /**
     * 获取xssfSheet
     * @param sheet
     * @return
     */
     static Sheet getXSSFSheet(Sheet sheet) {
        SXSSFSheet sxssfSheet ;
        if(sheet instanceof  SXSSFSheet){
            sxssfSheet = (SXSSFSheet) sheet;
        }else{
            return  sheet;
        }
        SXSSFWorkbook workbook = sxssfSheet.getWorkbook();
        XSSFWorkbook xssfWorkbook = workbook.getXSSFWorkbook();
        XSSFSheet sheet1 = xssfWorkbook.getSheet(sxssfSheet.getSheetName());
        return sheet1;
    }



    /**
     * 初始化sheet样式
     * @param sheetInfo
     */
    default   void initCellStyle(SheetHandlerInfo sheetInfo){

    }
    /**
     * 模板是否存在头部信息
     * @return
     */
    static boolean hasHeader(SheetHandlerInfo sheetInfo){
        if(sheetInfo.getHeaderRow()==sheetInfo.getStartRow()){
            return false;
        }
        return true;
    }
    /**
     * 获取头部的信息
     * @return
     */
     static Map<String,Integer> getSheetHeaders(SheetHandlerInfo sheetInfo){
         //不是标准的模板没有确定的头部
         if(sheetInfo.isNotStandard()){
             return null;
         }
         Map<String, Integer> map = new LinkedCaseInsensitiveMap<>();
         if(sheetInfo.getMaxColumnNum()>0){
             //模拟数字头部信息
             for (int i = 0; i < sheetInfo.getMaxColumnNum(); i++) {
                 map.put(i+"",i);
             }
         }else {
             //获取自己的sheet页的信息
             Sheet src = getXSSFSheet(sheetInfo.getSheet());
             //获取要比较的位置
             int headerRow = sheetInfo.getHeaderRow();
             int headerStartCol = sheetInfo.getHeaderStartCol();
             //获取String数据转换器
             CellDataToPropertyConvert instantiation = new StringCellDateToProperty();
             //获取2个的相同行
             Row row = src.getRow(headerRow);
             //获取它们的最后列的数量
             short lastCellNum = row.getLastCellNum();
             for (int i = headerStartCol; i < lastCellNum; i++) {
                 Cell cell = row.getCell(i);
                 if(cell==null){
                     continue;
                 }
                 String convert = (String) instantiation.convert(cell, String.class, null);
                 if (convert != null) {
                     map.put(convert.trim(), i);
                 }
             }
         }
         return map;
     }

    /**
     * 转换对象集合
     * @param condition
     * @return
     */
    List getObjects(Predicate<Sheet> condition);

    default List getObjects(){
        return getObjects(null);
    }

    /**
     * 转换指定对象
     * @param sheet
     * @param i
     * @return
     */
    Object getObject(Sheet sheet, int i);

    /**
     * 获取行映射器
     * @param row
     * @return
     */
    RowHandler getRowHandlerAdapter(Row row);

    /**
     * 写入
     * @param list
     */
    void write(List<?> list);

    /**
     * 写入扩展头部
     * @param sheet
     * @param currentValue
     */
    void writeExtendHeaders(Sheet sheet, Map<Integer, String> currentValue);


    /**
     * 比较sheet页是否一样
     * @param sheet 比较的sheet
     * @return
     */
    boolean comparison(SheetHandlerInfo sheet);

}
