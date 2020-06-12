package com.zhanghe.util.excel.sheet.row;


import com.zhanghe.autoconfig.annotation.type.OutType;
import com.zhanghe.util.excel.sheet.row.cell.CellDataToPropertyConvert;
import com.zhanghe.util.excel.sheet.row.cell.CommonCellConverterToProperty;
import com.zhanghe.util.excel.sheet.row.cell.MapPropertyConverter;
import com.zhanghe.util.excel.sheet.row.cell.property.CommonSetCell;
import com.zhanghe.util.excel.sheet.row.cell.property.MapPropertyToCells;
import com.zhanghe.util.excel.type.ExcelTypeWrap;
import com.zhanghe.util.excel.type.PropertyAndColumn;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
/*
 * TODO: 处理行信息适配器
 */
@Slf4j
public class RowHandlerAdapter implements RowHandler{
    /**
     * excel列数据到属性转换器
     */
   private CellDataToPropertyConvert convert = CommonCellConverterToProperty.instantiation();

    /**
     * 设置excel列值
     */
   private CommonSetCell propertyCellConvert = CommonSetCell.instantiation();

    private MapPropertyToCells mapPropertyToCells = new MapPropertyToCells();

   private RowHandlerInfo rowHandlerInfo;

    public RowHandlerAdapter(RowHandlerInfo rowHandlerInfo) {
        this.rowHandlerInfo = rowHandlerInfo;
    }

    /**
     * 获取对象
     * @return
     */
   public Object getObject(){
       Row row = rowHandlerInfo.getRow();
       //获取包装的类
       ExcelTypeWrap excelTypeWraps = rowHandlerInfo.getExcelTypeWraps();
       Class<?> aClass = excelTypeWraps.getAClass();
       if(ClassUtils.isAssignable(Map.class,aClass)){
           return getMapObject(row);
       }else {
           return getTObject(row, excelTypeWraps, aClass);
       }
   }

    private Object getTObject(Row row, ExcelTypeWrap excelTypeWraps, Class<?> aClass) {
        //实例化对象
        Object o = BeanUtils.instantiateClass(aClass);
        //用作解析操作bean的对象
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(o);
        //单独对map属性处理
        MapPropertyConverter mapPropertyConverter = new MapPropertyConverter(beanWrapper);
        //从包装类获取属性和excel的表格位置集合
        List<PropertyAndColumn> propertyAndColumns = excelTypeWraps.getPropertyAndColumns();
        //属性被改变的数量
        int propertyUpdateNum = 0;
        if (propertyAndColumns != null) {
            //遍历它
            for (PropertyAndColumn propertyAndColumn : propertyAndColumns) {
                //获取对应的单元格
                Cell cell = row.getCell(propertyAndColumn.getColumnIndex());
                if (cell == null||cell.getCellType()== CellType.BLANK) {
                    continue;
                }
//                if (log.isDebugEnabled()) {
//                    log.debug(String.format("当前下标在%s页%s行%s列数据为:%s", row.getSheet().getSheetName() + "", row.getRowNum() + "", propertyAndColumn.getColumnIndex() + "",cell+""));
//                }
                propertyUpdateNum ++;
                Class<?> type = propertyAndColumn.getType();
                if(mapPropertyConverter.canConvert(type)){
                    //直接处理这个map
                    mapPropertyConverter.convert(cell,type,propertyAndColumn);
                }else {
                    //转换指定类型
                    Object convert = this.convert.convert(cell, type, propertyAndColumn);
                    //设置属性值
                    beanWrapper.setPropertyValue(propertyAndColumn.getProperty(), convert);
                }
            }
        }
        if(propertyUpdateNum==0){
            return null;
        }
        return o;
    }

    private Object getMapObject(Row row) {
        Map map = new HashMap();
        short lastCellNum = row.getLastCellNum();
        short firstCellNum = row.getFirstCellNum();
        for (short i = firstCellNum;i<=lastCellNum;i++){
            Cell cell = row.getCell(i);
            Object convert = this.convert.convert(cell, Object.class,null);
            map.put(cell.getColumnIndex(),convert);
        }
        return map;
    }

    public void writeRow(Object o) {
        Row row = rowHandlerInfo.getRow();
        //获取包装的类
        ExcelTypeWrap excelTypeWraps = rowHandlerInfo.getExcelTypeWraps();
        handlerCells(o, row, excelTypeWraps);
    }

    public void handlerCells(Object o, Row row, ExcelTypeWrap excelTypeWraps) {
        CellStyle cellStyle = rowHandlerInfo.getSheetHandlerInfo().getCellStyle();
        handlerBackgroundColor(cellStyle);
        //用作解析操作bean的对象
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(o);
        //从包装类获取属性和excel的表格位置集合
        List<PropertyAndColumn> propertyAndColumns = excelTypeWraps.getPropertyAndColumns();
        if (propertyAndColumns != null) {
            //遍历它
            for (PropertyAndColumn propertyAndColumn : propertyAndColumns) {
                //先获取看有没有
                Cell cell = row.getCell(propertyAndColumn.getColumnIndex());
                //没有就创建处理
                if(cell==null) {
                    //获取对应的单元格
                    cell = row.createCell(propertyAndColumn.getColumnIndex());
                }
                indexHandler(propertyAndColumn,beanWrapper,row);
                //设置单元格的值
                Object propertyValue = beanWrapper.getPropertyValue(propertyAndColumn.getProperty());
                boolean b = mapPropertyToCells.canConvert(propertyAndColumn.getType(), null);
                if(b){
                    //扩展map属性
                    mapPropertyToCells.setAndStyle(cell,propertyValue,propertyAndColumn,cellStyle);
                }else {
                    //设置值并且给与样式
                    propertyCellConvert.setAndStyle(cell, propertyValue, propertyAndColumn, cellStyle);
                }
            }
        }
    }

    private void handlerBackgroundColor(CellStyle cellStyle) {
        if(cellStyle!=null){
            cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
            cellStyle.setFillBackgroundColor(IndexedColors.WHITE.index);
        }
    }

    private void indexHandler(PropertyAndColumn propertyAndColumn, BeanWrapper beanWrapper, Row row) {
        if(propertyAndColumn.getOutType()== OutType.INDEX){
            int rowNum = row.getRowNum()+1-getRowHandlerInfo().getSheetHandlerInfo().getStartRow();
            beanWrapper.setPropertyValue(propertyAndColumn.getProperty(),rowNum);
        }
    }
}
