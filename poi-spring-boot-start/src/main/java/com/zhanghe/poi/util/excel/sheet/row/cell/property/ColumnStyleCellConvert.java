package com.zhanghe.poi.util.excel.sheet.row.cell.property;

import com.zhanghe.poi.autoconfig.annotation.style.ColumnStyle;
import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.util.function.Consumer;

/**
 * @Author ZhangHe
 * @Description:
 * @Date 2020/2/21 11:06
 * @Version 1.0
 **/
public class ColumnStyleCellConvert implements PropertyToCellDataConvert {
   Logger logger = LoggerFactory.getLogger(ColumnStyleCellConvert.class);
    @Override
    public boolean setConvert(Cell cell, Object val, PropertyAndColumn propertyAndColumn) {
        ColumnStyle columnStyle = (ColumnStyle) val;
        Sheet sheet = cell.getSheet();
        //设置列样式
        Workbook workbook = cell.getSheet().getWorkbook();
        Consumer<CellStyle> cellStyleConsumer = columnStyle.getCellStyle();
        if(cellStyleConsumer!=null){
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyleConsumer.accept(cellStyle);
            cell.setCellStyle(cellStyle);
        }
        //设置行属性
        Row row = cell.getRow();
        Consumer<Row> rowProperty = columnStyle.getRowProperty();
        if(rowProperty!=null){
            rowProperty.accept(row);
        }

        Consumer<Cell> cellProperty = columnStyle.getCellProperty();
        if(cellProperty!=null){
            cellProperty.accept(cell);
        }

        Consumer<Font> fontProperty = columnStyle.getFontProperty();
        if(fontProperty!=null){
            CellStyle cellStyle = cell.getCellStyle();
            Font font = workbook.createFont();
            fontProperty.accept(font);
            cellStyle.setFont(font);
        }

        int columnIndex = cell.getColumnIndex();
        int rowIndex = cell.getRowIndex();
        int mergeRowNum = columnStyle.getMergeRowNum();
        int mergeColumnNum = columnStyle.getMergeColumnNum();

        CellRangeAddress cellAddresses = new CellRangeAddress(rowIndex,rowIndex+mergeRowNum,columnIndex,columnIndex+mergeColumnNum);
        try {
            sheet.addMergedRegion(cellAddresses);
        } catch (IllegalStateException e) {
            logger.error("error:合并区域重叠,合并样式添加失败!",e);
           // e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean canConvert(Class<?> c, PropertyAndColumn propertyAndColumn) {
        return ClassUtils.isAssignable(c, ColumnStyle.class);
    }
}
