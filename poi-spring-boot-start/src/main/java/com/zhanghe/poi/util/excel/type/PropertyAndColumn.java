package com.zhanghe.poi.util.excel.type;

import com.zhanghe.poi.autoconfig.annotation.type.OutType;
import lombok.Data;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * 对象的属性和对应的Excel列的信息
 * @author: ZhangHe
 * @since: 2020/4/22 17:09
 */
@Data
public class PropertyAndColumn {
    //所属封装
    private ExcelTypeWrap excelTypeWrap;
    //属性解析
    private PropertyDescriptor propertyDescriptor;
    //类的属性名称
    private String property;
    //对应的列的下标
    private int columnIndex;
    //对应的列的名称
    private String columnName;
    //该属性需求的类型
    private Class<?> type;
    //需求的格式化
    private String dateFormat;
    //输出的类型
    private OutType outType;
    //对应的列信息
    private Map<String,Object> excelColumn;
    //对应的行信息
    private Map<String,Object>  excelRow;
}
