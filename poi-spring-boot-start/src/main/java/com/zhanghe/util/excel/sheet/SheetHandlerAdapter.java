package com.zhanghe.util.excel.sheet;


import com.zhanghe.util.excel.sheet.row.RowHandlerAdapter;
import com.zhanghe.util.excel.sheet.row.RowHandlerInfo;
import com.zhanghe.util.excel.sheet.row.cell.CellDataToPropertyConvert;
import com.zhanghe.util.excel.sheet.row.cell.StringCellDateToProperty;
import com.zhanghe.util.excel.type.ExcelTypeWrap;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * 对sheet这个页处理的类
 */
@Data
@Log4j2
public class SheetHandlerAdapter extends AbstractSheetInfoType implements SheetHandler{

    public SheetHandlerAdapter(SheetHandlerInfo sheetInfo) {
        super(sheetInfo);
    }

    //初始化单元格样式
    @Override
    public void initCellStyle(SheetHandlerInfo sheetInfo) {

        if(SheetHandler.hasHeader(sheetInfo)&&sheetInfo.getCellStyle()==null) {
            if (!sheetInfo.isNoAutoStyle()) {
                Sheet sheet = SheetHandler.getXSSFSheet(sheetInfo.getSheet());
                int headerRow = sheetInfo.getHeaderRow();
                int headerStartCol = sheetInfo.getHeaderStartCol();
                Row row = sheet.getRow(headerRow);
                Assert.isTrue(row != null, "headerRow头部标题行指定位置错误!");
                Cell cell = row.getCell(headerStartCol);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setWrapText(true);
                sheetInfo.setCellStyle(cellStyle);
            }
        }
    }



    /**
     * 转换成需求的类型
     * @param condition 转换的条件
     *
     */
    @Override
    public List getObjects(Predicate<Sheet> condition){
        //是否往下执行
        boolean b = true;
        Sheet sheet = sheetInfo.getSheet();
        int rowNum = sheetInfo.getRowNum();
        int startRow = sheetInfo.getStartRow();
        if(condition!=null){
            b = condition.test(sheet);
        }
        //成立
        if(b){
            return getListObject(sheet, rowNum, startRow);
        }
        return null;
    }



    private List getListObject(Sheet sheet, int rowNum, int startRow) {
        List list = new ArrayList<>();
        //便利获取所有行
        for (int i=startRow;i<=rowNum;i++){
            Object object = getObject(sheet, i);
            if(object!=null)
                list.add(object);
        }
        return list;
    }
    @Override
    public Object getObject(Sheet sheet, int i) {
        Row row = sheet.getRow(i);
        RowHandlerAdapter rowHandlerAdapter = getRowHandlerAdapter(row);
        //获取对象
        return rowHandlerAdapter.getObject();
    }
    @Override
    public RowHandlerAdapter getRowHandlerAdapter(Row row) {
        RowHandlerInfo rowHandlerInfo = new RowHandlerInfo(excelTypeWrap,row);
        rowHandlerInfo.setSheetHandlerInfo(this.getSheetInfo());
        return new RowHandlerAdapter(rowHandlerInfo);
    }
    @Override
    public void write(List<?> list) {
        initCellStyle(sheetInfo);
        Sheet sheet = sheetInfo.getSheet();
        int startRow = sheetInfo.getStartRow();
        //获取扩展头部信息
        Map<Integer, String> currentValue = ExcelTypeWrap.getCurrentValue();
        writeExtendHeaders(sheet,currentValue);
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            int rowIndex = startRow+i;
            Row row = sheet.createRow(rowIndex);
            RowHandlerAdapter rowHandlerAdapter = getRowHandlerAdapter(row);
            rowHandlerAdapter.writeRow(o);
        }
        setAutoSizeColumn();
        ExcelTypeWrap.cleanCurrentValue();
    }

    /**
     * 写入扩展头部标题
     * @param sheet sheet页
     * @param currentValue 当前值
     */
    @Override
    public void writeExtendHeaders(Sheet sheet, Map<Integer, String> currentValue) {
        initCellStyle(sheetInfo);
        Sheet  xssfSheet = SheetHandler.getXSSFSheet(sheet);
        if(currentValue!=null){
            int headerRow = sheetInfo.getHeaderRow();
            Row row = xssfSheet.getRow(headerRow);
            short lastCellNum = row.getLastCellNum();
            //从最后一行开始一个个的添加扩展头部标题信息
            currentValue.forEach((k,v)->{
                if(k>=lastCellNum){
                    Cell cell = row.createCell(k);
                    cell.setCellValue(v);
                    cell.setCellStyle(sheetInfo.getCellStyle());
                }
            });
        }
    }


    /**
     * 自适应大小
     */
    private void setAutoSizeColumn() {
        int headerRow = getSheetInfo().getHeaderRow();
        int headerStartCol = getSheetInfo().getHeaderStartCol();
        Sheet xssfSheet = SheetHandler.getXSSFSheet(getSheetInfo().getSheet());
        Row row = Optional.ofNullable(xssfSheet.getRow(headerRow)).orElseGet(()->getSheetInfo().getSheet().getRow(headerRow));
        short lastCellNum = row.getLastCellNum();
        Sheet sheet =getSheetInfo().getSheet();
        if(sheet instanceof SXSSFSheet){
            //跟踪列
            ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
        }
        for (int i = headerStartCol; i <lastCellNum ; i++) {
            sheet.autoSizeColumn(i);
            int columnWidth = sheet.getColumnWidth(i);
            //单元格单行最长支持255*256宽度（每个单元格样式已经设置自动换行，超出即换行）
            //最低宽度为6个单元中文字符
            int width = Math.max(15*256,Math.min(255*256,columnWidth*12/10));
            sheet.setColumnWidth(i,width);
        }
    }


    /**
     * 比较sheet页是否一样
     * @param sheet 比较的sheet
     *
     */
    public boolean comparison(SheetHandlerInfo sheet){
        //没有头部直接true
        if(!SheetHandler.hasHeader(this.getSheetInfo())){
            return true;
        }
        //获取自己的sheet页的信息
        SheetHandlerInfo sheetInfo = this.getSheetInfo();
        //分别获取自己和比较的sheet
        Sheet src = sheetInfo.getSheet();
        Sheet target = sheet.getSheet();
        //获取要比较的位置
        int headerRow = sheetInfo.getHeaderRow();
        int headerStartCol = sheetInfo.getHeaderStartCol();
        //获取String数据转换器
        CellDataToPropertyConvert instantiation = new StringCellDateToProperty();
        //获取2个的相同行
        Row row = src.getRow(headerRow);
        Row row1 = target.getRow(headerRow);
        //获取它们的最后列的数量
        short lastCellNum = row.getLastCellNum();
        short lastCellNum1 = row1.getLastCellNum();
        //相同就继续下去
        if(lastCellNum==lastCellNum1) {
            //循环比较是否相等
            for (int i = headerStartCol; i < lastCellNum; i++) {
                Cell cell = row.getCell(i);
                Cell cell1 = row1.getCell(i);
                String convert = (String) instantiation.convert(cell, String.class, null);
                String convert2 = (String) instantiation.convert(cell1, String.class, null);
                if(!(convert!=null&&convert.equals(convert2))){
                    if(convert==null&&convert2==null){
                        continue;
                    }
                    log.error("请检查对比列:"+convert+"<>"+convert2);
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
