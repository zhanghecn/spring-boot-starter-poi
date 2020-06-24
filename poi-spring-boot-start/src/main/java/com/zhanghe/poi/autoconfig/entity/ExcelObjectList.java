package com.zhanghe.poi.autoconfig.entity;

import java.util.LinkedList;
import java.util.List;

public class ExcelObjectList<T> extends LinkedList<ExcelObjectData<T>> {
    public List<T> getAllList(){
        List list = new LinkedList();
        for (ExcelObjectData<T> tExcelObjectData : this) {
            list.addAll(tExcelObjectData.getList());
        }
        return list;
    }
}
