package com.zhanghe.poi.util.excel.sheet;


import com.zhanghe.poi.util.excel.sheet.row.FixedRowColumnHandler;
import com.zhanghe.poi.util.excel.sheet.row.RowHandler;
import com.zhanghe.poi.util.excel.type.ExcelTypeWrap;
import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.*;
import java.util.function.Predicate;

/**
 * 对于不是常规的excel页处理
 * 不规范的不是一行行的，所以一个sheet代表一个对象
 * @Author: ZhangHe
 * @Date: 2020/5/26 15:07
 */
@Slf4j
public class SheetNotStandardHandlerAdapter extends AbstractSheetInfoType implements  SheetHandler{
    /**
     * 存放所有的行，已经排序过了的
     */
    private List<Row> rows;
    /**
     * 行列映射
     */
    private Map<Integer,List<PropertyAndColumn>> rowColumns;

    private RowHandler rowHandler;

    public SheetNotStandardHandlerAdapter(SheetHandlerInfo sheetInfo) {
        super(sheetInfo);
        initRows();
    }

    /**
     * 初始化行信息
     */
    private void initRows() {
        this.rows = new ArrayList<>();
        this.rowColumns = new HashMap<>();
        //获取所有行
        ExcelTypeWrap excelTypeWrap = this.getExcelTypeWrap();
        List<PropertyAndColumn> propertyAndColumns = excelTypeWrap.getPropertyAndColumns();
        Set<Integer> set = new TreeSet<>();

        for (PropertyAndColumn propertyAndColumn : propertyAndColumns) {
            Map<String, Object> excelRow = propertyAndColumn.getExcelRow();
            Integer row = (Integer) excelRow.get("rowIndex");
            set.add(row);
            putColumn(propertyAndColumn, row);
        }
        //添加行
        Sheet sheet = getSheetInfo().getSheet();
        for (Integer r : set) {
            Row row = sheet.getRow(r);
            rows.add(row);
        }
    }

    /**
     * 对应行里面的列信息添加
     * @param propertyAndColumn
     * @param row
     */
    private void putColumn(PropertyAndColumn propertyAndColumn, Integer row) {
        List<PropertyAndColumn> orDefault = this.rowColumns.getOrDefault(row, new ArrayList<>());
        orDefault.add(propertyAndColumn);
        this.rowColumns.put(row,orDefault);
    }

    @Override
    public List getObjects(Predicate<Sheet> condition) {
       boolean b = true;
        if(condition!=null){
           b = condition.test(this.sheetInfo.getSheet());
       }

        if(b){
            List list = new ArrayList();
            list.add(getObject(this.sheetInfo.getSheet(),-1));
            return list;
        }
        return null;
    }

    @Override
    public Object getObject(Sheet sheet, int i) {
        Class<?> aClass = this.getSheetInfo().getTClass();
        //实例化对象
        Object o = BeanUtils.instantiateClass(aClass);
        //用作解析操作bean的对象
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(o);
        Map<Integer, List<PropertyAndColumn>> rowColumns = this.rowColumns;
        //遍历所有列信息
        rowColumns.forEach((x, y)->{
            for (PropertyAndColumn propertyAndColumn : y) {
                FixedRowColumnHandler rowHandlerAdapter = (FixedRowColumnHandler) getRowHandlerAdapter(null);
                rowHandlerAdapter.setRowIndex(x);
                rowHandlerAdapter.setPropertyAndColumn(propertyAndColumn);
                Object object = rowHandlerAdapter.getObject();
                beanWrapper.setPropertyValue(propertyAndColumn.getProperty(),object);
            }
        });
        return o;
    }

    @Override
    public RowHandler getRowHandlerAdapter(Row row) {
        return new FixedRowColumnHandler(sheetInfo);
    }

    @Override
    public void write(List<?> list) {
        Map<Integer, List<PropertyAndColumn>> rowColumns = this.rowColumns;

        for (Object o : list) {
            assertType(o);
            BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(o);
            //遍历所有行列信息
            rowColumns.forEach((x, y)->{
                for (PropertyAndColumn propertyAndColumn : y) {
                    //写入列的值
                    FixedRowColumnHandler rowHandlerAdapter = (FixedRowColumnHandler) getRowHandlerAdapter(null);
                    rowHandlerAdapter.setRowIndex(x);
                    rowHandlerAdapter.setPropertyAndColumn(propertyAndColumn);
                    rowHandlerAdapter.writeRow(beanWrapper);
                }
            });
        }

    }

    /**
     * 校验类型是否为配置的类型
     * @param o
     */
    protected void assertType(Object o) {
        Class<?> aClass = o.getClass();
        Class<?> tClass = sheetInfo.getTClass();
        Assert.isTrue(ClassUtils.isAssignable(tClass,aClass),String.format("类型:%s不属于%s",aClass,tClass));
    }

    @Override
    public void writeExtendHeaders(Sheet sheet, Map<Integer, String> currentValue) {

    }

    @Override
    public boolean comparison(SheetHandlerInfo sheet) {
        return true;
    }
}
