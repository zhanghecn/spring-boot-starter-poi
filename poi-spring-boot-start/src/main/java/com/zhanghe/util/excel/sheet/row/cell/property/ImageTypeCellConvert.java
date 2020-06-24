package com.zhanghe.util.excel.sheet.row.cell.property;

import com.zhanghe.autoconfig.annotation.type.ImageType;
import com.zhanghe.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 图片类型cell转换
 */
public class ImageTypeCellConvert implements PropertyToCellDataConvert {
    @Override
    public boolean setConvert(Cell cell, Object val,  PropertyAndColumn propertyAndColumn) {
        List<ImageType> imageTypes = new ArrayList<>();
        if(isImageType(propertyAndColumn)){
            imageTypes.add((ImageType) val);
        }else{
            imageTypes.addAll((Collection<? extends ImageType>) val);
        }
        Sheet sheet = cell.getSheet();

        Workbook workbook = sheet.getWorkbook();

        Drawing<?> drawingPatriarch = getDrawing(sheet);
        //获取行列下标
        Map<String, Object> excelRow = propertyAndColumn.getExcelRow();
        int columnIndex = propertyAndColumn.getColumnIndex();
        Integer rowIndex =excelRow.get("rowIndex")==null?0: (Integer) excelRow.get("rowIndex");
        for (ImageType imageType : imageTypes) {
            if(ObjectUtils.isEmpty(imageType.getData())){
                continue;
            }
            ClientAnchor anchor;
            //自定义位置
            if(imageType.isCustomOrientation()){
                anchor=   drawingPatriarch.createAnchor(0, 0, 0, 0, imageType.getX(), imageType.getY(), imageType.getX2(), imageType.getY2());
            }else{
                anchor = drawingPatriarch.createAnchor(0, 0, 0, 0, columnIndex, rowIndex, columnIndex+5, rowIndex+10);
            }
            int i = workbook.addPicture(imageType.getData(), imageType.getImageType());
            drawingPatriarch.createPicture(anchor,i);
        }
        return true;
    }

    protected Drawing<?> getDrawing(Sheet sheet) {
        Drawing<?> drawingPatriarch = sheet.getDrawingPatriarch();
        if(drawingPatriarch==null){
            drawingPatriarch = sheet.createDrawingPatriarch();
        }
        return drawingPatriarch;
    }

    /**
     * 是否为图片类型
     * @param propertyAndColumn 属性和列
     * @return 是图片类型
     */
    protected boolean isImageType(PropertyAndColumn propertyAndColumn){
        PropertyDescriptor propertyDescriptor = propertyAndColumn.getPropertyDescriptor();
        Class<?> propertyType = propertyDescriptor.getPropertyType();
        //图片类型的父类
        return ClassUtils.isAssignable(ImageType.class,propertyType);
    }

    /**
     * 是否为集合图片类型
     * @param propertyAndColumn 属性和列
     * @return 是集合图片类型
     */
    protected boolean isListImageType(PropertyAndColumn propertyAndColumn){
        PropertyDescriptor propertyDescriptor = propertyAndColumn.getPropertyDescriptor();
        Class<?> propertyType = propertyDescriptor.getPropertyType();
        //图片类型的父类
        //属于list
        if(ClassUtils.isAssignable(List.class, propertyType)){
            ResolvableType resolvableType = ResolvableType.forMethodParameter(propertyDescriptor.getWriteMethod(),0);
            ResolvableType generic = resolvableType.getGeneric(0);
            return ClassUtils.isAssignable(ImageType.class,generic.getRawClass());
        }
        return false;
    }
    @Override
    public boolean canConvert(Class<?> c, PropertyAndColumn propertyAndColumn) {
        return isImageType(propertyAndColumn)||isListImageType(propertyAndColumn);
    }
}
