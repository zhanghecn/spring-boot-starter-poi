package com.zhanghe.util.excel.sheet.row.cell;

import com.zhanghe.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.ClassUtils;


public class PrimitiveAndWrapCellDateConverterToProperty implements CellDataToPropertyConvert {
    private DefaultConversionService defaultConversionService;
    public PrimitiveAndWrapCellDateConverterToProperty(){
        defaultConversionService =  (DefaultConversionService) DefaultConversionService.getSharedInstance();
    }
    @Override
    public Object convert(Cell cell, Class<?> c, PropertyAndColumn propertyAndColumn) {
        //获取原先的值
        String numericCellValue = cell.toString();
        Double aDouble = defaultConversionService.convert(numericCellValue, Double.class);
        //和需要的类型转换
        Object convert = defaultConversionService.convert(aDouble, c);
        return convert;
    }

    @Override
    public boolean canConvert(Class<?> c) {
        boolean primitiveOrWrapper = ClassUtils.isPrimitiveOrWrapper(c);
        if(primitiveOrWrapper&&c!=void.class&&c!=Void.class){
            return true;
        }
        return false;
    }
}
