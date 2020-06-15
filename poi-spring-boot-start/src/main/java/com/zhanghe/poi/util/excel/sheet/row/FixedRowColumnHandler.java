package com.zhanghe.poi.util.excel.sheet.row;


import com.zhanghe.poi.util.excel.sheet.SheetHandlerInfo;
import com.zhanghe.poi.util.excel.sheet.row.cell.CellDataToPropertyConvert;
import com.zhanghe.poi.util.excel.sheet.row.cell.CommonCellConverterToProperty;
import com.zhanghe.poi.util.excel.sheet.row.cell.property.CommonSetCell;
import com.zhanghe.poi.util.excel.sheet.row.cell.property.PropertyToCellDataConvert;
import com.zhanghe.poi.util.excel.type.ExcelTypeWrap;
import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanWrapper;

/**
 * 固定行列获取
 * @Author: ZhangHe
 * @Date: 2020/5/26 17:01
 */
@Data
public class FixedRowColumnHandler implements RowHandler {
    //读取转换
    CellDataToPropertyConvert cellDataToPropertyConvert =  CommonCellConverterToProperty.instantiation();
    //写出转换
    PropertyToCellDataConvert propertyToCellDataConvert = CommonSetCell.instantiation();

    private PropertyAndColumn propertyAndColumn;

    private SheetHandlerInfo info;

    private int rowIndex;



    public FixedRowColumnHandler(SheetHandlerInfo info) {
        this.info = info;
    }

    /**
     * 与平常的行对象不同，这个行对象就是列的值
     * @return
     */
    @Override
    public Object getObject() {
        Sheet sheet = info.getSheet();
        Row row = sheet.getRow(rowIndex);
        if(row!=null){
            int columnIndex = propertyAndColumn.getColumnIndex();
            Cell cell = row.getCell(columnIndex);
            return cellDataToPropertyConvert.convert(cell,propertyAndColumn.getType(),propertyAndColumn);
        }
        return null;
    }

    public XSSFSheet getXSSFSheet(){
        XSSFWorkbook xsSfWorkbook = getXSSfWorkbook();
         return xsSfWorkbook.getSheet(this.info.getSheet().getSheetName());
    }
    public XSSFWorkbook getXSSfWorkbook(){
        Sheet sheet = this.info.getSheet();
        if(sheet instanceof SXSSFSheet){
            SXSSFSheet sxssfSheet = (SXSSFSheet) sheet;
            return sxssfSheet.getWorkbook().getXSSFWorkbook();
        }
        return (XSSFWorkbook) sheet.getWorkbook();
    }

    /**
     * 与平常写入对象一行不同，这个就是写入一列的值
     * @param o
     */
    @Override
    public void writeRow(Object o) {

        Sheet sheet = info.getSheet();

        Row row = getRow(sheet);

        int columnIndex = propertyAndColumn.getColumnIndex();
        Cell cell1 = getCell(row, columnIndex);
        if(o instanceof BeanWrapper){
            BeanWrapper beanWrapper = (BeanWrapper) o;
            Object propertyValue = beanWrapper.getPropertyValue(propertyAndColumn.getProperty());
            propertyToCellDataConvert.setConvert(cell1,propertyValue,propertyAndColumn);
        }
    }

    protected Row getRow(Sheet sheet) {
        Row row =  getXSSFSheet().getRow(rowIndex);
        if(row==null){
            row=   sheet.createRow(rowIndex);
        }
        return row;
    }

    protected void removeXSSFRow() {
        XSSFRow xssfRow = getXSSFSheet().getRow(rowIndex);
        if(xssfRow!=null) {
            getXSSFSheet().removeRow(xssfRow);
        }
    }

    protected Cell getCell(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if(cell==null){
            cell=  row.createCell(columnIndex);
        }
        return cell;
    }

    @Override
    public void handlerCells(Object o, Row row, ExcelTypeWrap excelTypeWraps) {

    }
}
