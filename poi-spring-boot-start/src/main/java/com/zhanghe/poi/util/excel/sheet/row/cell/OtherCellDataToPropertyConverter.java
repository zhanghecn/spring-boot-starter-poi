package com.zhanghe.poi.util.excel.sheet.row.cell;

import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

//剩下的转换
public class OtherCellDataToPropertyConverter implements CellDataToPropertyConvert {
    ConversionService defaultConversionService = DefaultConversionService.getSharedInstance();
    @Override
    public Object convert(Cell cell, Class<?> c, PropertyAndColumn propertyAndColumn) {
        Object moderateData = getModerateData(cell);
        if(moderateData!=null){
            Class<?> aClass = moderateData.getClass();
            boolean b = ClassUtils.isAssignable(c,aClass)|| defaultConversionService.canConvert(aClass, c);
            //可以转换
            if(b){
                Object convert = defaultConversionService.convert(moderateData, c);
                return convert;
            }
        }
        Assert.isTrue(false,String.format("有列无法完成数据转换,类型为:%s,在第%d行,第%d列",c+""
                ,cell.getRow().getRowNum(),cell.getColumnIndex())
        );
        return null;
    }
    public Object getModerateData(Cell cell){
        Object o =null;
        try {
            //第一步获取可能是日期值
            o = cell.getDateCellValue();
        } catch (Exception e) {
            //发生了异常，以此获取值
            if(cell.getCellType() == CellType.BOOLEAN){
              o=  cell.getBooleanCellValue();
            }else if(cell.getCellType() == CellType.NUMERIC){
                o=cell.getNumericCellValue();
            }else if(cell.getCellType() == CellType.FORMULA){
                o=cell.getCellFormula();
            }else if(cell.getCellType()== CellType.ERROR){
                o = cell.getErrorCellValue();
            }else{
                o= cell.getStringCellValue();
            }
        }
        return o;
    }
    @Override
    public boolean canConvert(Class<?> c) {
        return true;
    }
}
