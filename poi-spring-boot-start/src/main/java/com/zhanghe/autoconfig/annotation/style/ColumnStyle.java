package com.zhanghe.autoconfig.annotation.style;

import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import java.util.function.Consumer;

/**
 * @Author ZhangHe
 * @Description: excel列样式
 * @Date 2020/2/20 17:28
 * @Version 1.0
 **/
@Data
public class ColumnStyle {
    //合并的行数
    private int mergeRowNum;

    //合并的列数
    private int mergeColumnNum;

    //设置列样式
    private Consumer<CellStyle> cellStyle;

    //设置行属性
    private Consumer<Row> rowProperty;

    //设置列属性
    private Consumer<Cell> cellProperty;

    //设置字体
    private Consumer<Font> fontProperty;

    public ColumnStyle(int mergeRowNum, int mergeColumnNum) {
        this.mergeRowNum = mergeRowNum;
        this.mergeColumnNum = mergeColumnNum;
    }

    public ColumnStyle() {
    }
}
