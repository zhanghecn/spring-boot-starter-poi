package com.zhanghe.poi.util.excel.sheet.row.cell;

import com.zhanghe.poi.util.SpringContextHelper;
import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;

import java.util.LinkedList;

public class CommonCellConverterToProperty implements CellDataToPropertyConvert {
    private LinkedList<CellDataToPropertyConvert> cellDataToPropertyConverts;

    @Override
    public boolean isConvertColumn(PropertyAndColumn propertyAndColumn) {
        return getCellDataToPropertyConvert(null,propertyAndColumn)!=null;
    }

    private static CommonCellConverterToProperty commonCellConverterToProperty;
    //TODO:按顺序注册
    public CommonCellConverterToProperty() {
    }

    private void addDefaultConverts() {
        this.cellDataToPropertyConverts =new LinkedList<>();
        this.cellDataToPropertyConverts.add(new PropertyIgnore());
        this.cellDataToPropertyConverts.add(new PrimitiveAndWrapCellDateConverterToProperty());
        this.cellDataToPropertyConverts.add(new NumberCellToProperty());
        this.cellDataToPropertyConverts.add(new DateCellFormatConverterToProperty());
        this.cellDataToPropertyConverts.add(new StringCellDateToProperty());
        this.cellDataToPropertyConverts.add(new FixedColumnRowConvert());
        addSpringBeanCellConverts();
        this.cellDataToPropertyConverts.add(new OtherCellDataToPropertyConverter());
    }

    /**
     * 添加spring bean 里面的转换器
     */
    protected void addSpringBeanCellConverts() {
        ApplicationContext app = SpringContextHelper.applicationContext;
        String[] beanNamesForType = app.getBeanNamesForType(CellDataToPropertyConvert.class);
        if(!ObjectUtils.isEmpty(beanNamesForType)){
            for (String s : beanNamesForType) {
                CellDataToPropertyConvert bean = (CellDataToPropertyConvert) app.getBean(s);
                this.cellDataToPropertyConverts.add(bean);
            }
        }
    }

    public CellDataToPropertyConvert getCellDataToPropertyConvert(Class<?> c, PropertyAndColumn propertyAndColumn){
        for (CellDataToPropertyConvert cellDataToPropertyConvert : cellDataToPropertyConverts) {
            //如果此项可以用
            if((c!=null &&  cellDataToPropertyConvert.canConvert(c))||
                    (propertyAndColumn!=null&&cellDataToPropertyConvert.isConvertColumn(propertyAndColumn))){
                return cellDataToPropertyConvert;
            }
        }
        return null;
    }
    public void addCellDataToPropertyConvert(CellDataToPropertyConvert c){
        //优先使用自定义的
        cellDataToPropertyConverts.addFirst(c);
    }
    public static synchronized CellDataToPropertyConvert instantiation(){
        if(commonCellConverterToProperty==null){
            commonCellConverterToProperty = new CommonCellConverterToProperty();
            commonCellConverterToProperty.addDefaultConverts();
        }
        return commonCellConverterToProperty;
    }

    @Override
    public Object convert(Cell cell, Class<?> c, PropertyAndColumn propertyAndColumn) {
        CellDataToPropertyConvert cellDataToPropertyConvert = getCellDataToPropertyConvert(c,propertyAndColumn);
        return cellDataToPropertyConvert.convert(cell,c,propertyAndColumn);
    }


    @Override
    public boolean canConvert(Class<?> c) {
        return getCellDataToPropertyConvert(c,null)!=null;
    }
}
