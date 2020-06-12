package com.zhanghe.autoconfig.annotation.type;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.PictureData;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Excel图片类型
 * @Author: ZhangHe
 * @Date: 2020/5/25 10:28
 */
@Data
@NoArgsConstructor
public class ImageType {

    private int width;

    private int height;

    private Picture picture;

    private byte[] data;

    private int x;

    private int y;

    private int x2;

    private int y2;

    /**
     * 图片类型
     */
    private int imageType;

    /** Extended windows meta file */
   public static final int PICTURE_TYPE_EMF = 2;

    /** Windows Meta File */
    public static final int PICTURE_TYPE_WMF = 3;

    /** Mac PICT format */
    public static final int PICTURE_TYPE_PICT = 4;

    /** JPEG format */
    public static final int PICTURE_TYPE_JPEG = 5;

    /** PNG format */
    public static final int PICTURE_TYPE_PNG = 6;

    /** Device independent bitmap */
    public static final int PICTURE_TYPE_DIB = 7;

    /**
     * 是否自定义图片位置
     */
    private boolean customOrientation;

    public ImageType(int x,int y,int x2,int y2, byte[] data,int imageType) {
        this.data = data;
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.imageType = imageType;
        this.customOrientation=true;
    }

    public ImageType(byte[] data,int imageType) {
        this.data = data;
        this.imageType = imageType;
    }

    public static ArrayList<ImageType> imageTypeList(int imageType,byte[]... data){
        if(data==null){
            return null;
        }
        return imageTypeList(imageType, Arrays.asList(data));
    }

    public static ArrayList<ImageType> imageTypeList(int imageType, Collection<byte[]> bytes){
        if(bytes==null||bytes.isEmpty()){
            return null;
        }
        int col = 0;
        int row = 0;
        ArrayList<ImageType> arrayList = new ArrayList<>(bytes.size());
        for (byte[] datum : bytes) {
            ImageType imageType1 = new ImageType(col,row,col+5,row+10,datum,imageType);
            arrayList.add(imageType1);
            col+=2;
            row+=2;
        }
        return arrayList;
    }
    /**
     * 一般导入的时候获取图片信息
     * @param picture
     */
    public ImageType(Picture picture) {
        PictureData pictureData = picture.getPictureData();
        Dimension imageDimension = picture.getImageDimension();
        ClientAnchor preferredSize = picture.getPreferredSize();
        this.picture =  picture;
        this.data =     pictureData.getData();
        this.width =    imageDimension.width;
        this.height=    imageDimension.height;
        this.x = preferredSize.getDx1();
        this.y = preferredSize.getDy1();
        this.x2 =preferredSize.getDx2();
        this.y2 = preferredSize.getDy2();
    }
}

