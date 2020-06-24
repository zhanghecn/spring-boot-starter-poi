package com.zhanghe.poi.util.excel.sheet.row.cell.property;

import com.zhanghe.poi.util.SpringContextHelper;
import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;

import java.util.LinkedList;

public class CommonSetCell implements PropertyToCellDataConvert {
    private LinkedList<PropertyToCellDataConvert> propertyToCellDataConverts;
    private static CommonSetCell commonSetCell;
    //TODO:按顺序注册
    public CommonSetCell() {
    }

    protected void addDefaultConverts() {
        this.propertyToCellDataConverts =new LinkedList<>();
        this.propertyToCellDataConverts.add(new DoubleCellConverter());
        this.propertyToCellDataConverts.add(new NumberCellConverter());
        this.propertyToCellDataConverts.add(new BooleanCellConvert());
        this.propertyToCellDataConverts.add(new CalendarCellConverter());
        this.propertyToCellDataConverts.add(new DateCellConvert());
        this.propertyToCellDataConverts.add(new ImageTypeCellConvert());
        this.propertyToCellDataConverts.add(new ColumnStyleCellConvert());
        this.addSpringBeanCellConverts();
        this.propertyToCellDataConverts.add(new StringCellConvert());
    }

    /**
     * 添加spring bean 里面的转换器
     */
    protected void addSpringBeanCellConverts() {
        ApplicationContext app = SpringContextHelper.applicationContext;
        String[] beanNamesForType = app.getBeanNamesForType(PropertyToCellDataConvert.class);
        if(!ObjectUtils.isEmpty(beanNamesForType)){
            for (String s : beanNamesForType) {
                PropertyToCellDataConvert bean = (PropertyToCellDataConvert) app.getBean(s);
                this.propertyToCellDataConverts.add(bean);
            }
        }
    }

    public void addCellDataToPropertyConvert(PropertyToCellDataConvert c){
        //优先使用自定义的
        propertyToCellDataConverts.addFirst(c);
    }
    public void setStyle(Cell cell, CellStyle cellStyle) {
        if(cellStyle!=null) {
            if(cellStyle instanceof XSSFCellStyle){
                XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
                cellStyle = (CellStyle) xssfCellStyle.clone();
            }
            //居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellStyle(cellStyle);
        }
    }

    public PropertyToCellDataConvert getPropertyToCellDataConvert(Class<?> c, PropertyAndColumn propertyAndColumn){
        for (PropertyToCellDataConvert propertyToCellDataConvert : propertyToCellDataConverts) {
            if(propertyToCellDataConvert.canConvert(c,propertyAndColumn)){
                return propertyToCellDataConvert;
            }
        }
        return null;
    }
    public static synchronized CommonSetCell instantiation(){
        if(commonSetCell==null){
            commonSetCell = new CommonSetCell();
            commonSetCell.addDefaultConverts();
        }
        return commonSetCell;
    }

    @Override
    public boolean setConvert(Cell cell, Object val, PropertyAndColumn propertyAndColumn) {
        if(val!=null){
            Class<?> aClass = val.getClass();
            for (PropertyToCellDataConvert propertyToCellDataConvert : propertyToCellDataConverts) {
                if(propertyToCellDataConvert.canConvert(aClass,propertyAndColumn)){
                    boolean b = propertyToCellDataConvert.setConvert(cell,val,propertyAndColumn);
                    if(b){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 设置值，并且给与样式
     * @param cell
     * @param val
     * @param propertyAndColumn
     * @param cellStyle
     * @return
     */
    public boolean setAndStyle(Cell cell, Object val, PropertyAndColumn propertyAndColumn, CellStyle cellStyle) {
        setStyle(cell,cellStyle);
       return setConvert(cell,val,propertyAndColumn);
    }
    @Override
    public boolean canConvert(Class<?> c, PropertyAndColumn propertyAndColumn) {
        return getPropertyToCellDataConvert(c,propertyAndColumn)!=null;
    }
}
