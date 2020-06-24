package com.zhanghe.poi.util.excel.sheet.row.cell;

import com.zhanghe.poi.autoconfig.annotation.type.ImageType;
import com.zhanghe.poi.util.excel.type.PropertyAndColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Shape;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 其他资源固定行列转换 如图片等
 * @Author: ZhangHe
 * @Date: 2020/5/26 15:58
 */
public class FixedColumnRowConvert implements CellDataToPropertyConvert {
    @Override
    public Object convert(Cell cell, Class<?> c, PropertyAndColumn propertyAndColumn) {

        PropertyDescriptor propertyDescriptor = propertyAndColumn.getPropertyDescriptor();

        Class<?> propertyType = propertyDescriptor.getPropertyType();

        List<ImageType> images = getImages(cell);
        if(!images.isEmpty()){
            if(ClassUtils.isAssignable(propertyType,ImageType.class)){
                Map<String, Object> excelRow = propertyAndColumn.getExcelRow();

                Integer rowIndex = (Integer) excelRow.get("rowIndex");

                int columnIndex = propertyAndColumn.getColumnIndex();

                //找出匹配到的位置图片
                ImageType imageType = images.parallelStream().filter((x) -> x.getX() == columnIndex && x.getY() == rowIndex).findAny().orElseGet(() -> images.get(0));

                return imageType;
            }
            if(ClassUtils.isAssignable(propertyType, List.class)){
                return images;
            }
        }
        return null;
    }

    protected  List<ImageType> getImages(Cell cell) {
        List<ImageType> imageTypes = new ArrayList<>();
        Drawing<Shape> drawingPatriarch = (Drawing<Shape>) cell.getSheet().getDrawingPatriarch();
        drawingPatriarch.forEach((x)->{
            if(x instanceof Picture){
                Picture p = (Picture) x;
                ImageType imageType = new ImageType(p);
                imageTypes.add(imageType);
            }
        });
        return imageTypes;
    }

    @Override
    public boolean canConvert(Class<?> c) {
        return false;
    }

    @Override
    public boolean isConvertColumn(PropertyAndColumn propertyAndColumn) {
        boolean isFixed = propertyAndColumn.getExcelRow() != null && !propertyAndColumn.getExcelRow().isEmpty();
        if(isFixed){
            //获取类的泛型
            PropertyDescriptor propertyDescriptor = propertyAndColumn.getPropertyDescriptor();
            Class<?> propertyType = propertyDescriptor.getPropertyType();
            if(ClassUtils.isAssignable(propertyType,ImageType.class)){
                return true;
            }
            if(ClassUtils.isAssignable(List.class, propertyType)){
                return true;
            }
        }
        //存在行信息，代表已经固定了
        return false;
    }
}
