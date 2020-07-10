package com.zhanghe.poi.autoconfig.annotation.style;

import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import java.util.function.Consumer;

/**
 * @author ZhangHe
 * @since 2020/2/20 17:28
 *  1.0
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

    public static StyleBuilder styleBuilder(){
        return new StyleBuilder();
    }
    public static class StyleBuilder{
        private ColumnStyle columnStyle;
        public StyleBuilder() {
            columnStyle = new ColumnStyle();
        }

        public StyleBuilder cellStyle(Consumer<CellStyle> cellStyleConsumer){
            columnStyle.cellStyle = cellStyleConsumer;
            return this;
        }
        public StyleBuilder rowProperty(Consumer<Row> rowConsumer){
            columnStyle.rowProperty = rowConsumer;
            return this;
        }
        public StyleBuilder cellProperty(Consumer<Cell> cellConsumer){
            columnStyle.cellProperty = cellConsumer;
            return this;
        }

        public StyleBuilder fontProperty(Consumer<Font> fontConsumer){
            columnStyle.fontProperty = fontConsumer;
            return this;
        }

        public StyleBuilder mergeRowColumn(int mergeRowNum, int mergeColumnNum){
            columnStyle.mergeColumnNum =  mergeColumnNum;
            columnStyle.mergeColumnNum = mergeColumnNum;
            return this;
        }
        public ColumnStyle build(){
            return columnStyle;
        }
    }
}
