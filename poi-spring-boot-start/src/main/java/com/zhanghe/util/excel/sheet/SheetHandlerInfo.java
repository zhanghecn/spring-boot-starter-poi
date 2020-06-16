package com.zhanghe.util.excel.sheet;

import lombok.Data;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;

/**
 * 要处理sheet页的信息
 */
@Data
public class SheetHandlerInfo {
    //这个sheet来自那个文件
    private String fileName;

    private InputStream inputStream;

    private Sheet sheet;
    //开始读取的行
    private int startRow;
    //总的行数量
    private  int rowNum;
    //要读取成的类型
    private Class<?> tClass;
    //头部的行
    private int headerRow;
    //头部开始的列
    private int headerStartCol;
    //每个单元格的样式
    private CellStyle cellStyle;
    /**
     * 用作没有模板读取或者导出的时候指定最多列数量
     */
    private int maxColumnNum;

    /**
     * 是否取消自动样式用来提高导出效率
     */
    private boolean noAutoStyle;

    /**
     * 不是一个标准模板？
     */
    private boolean notStandard;

    /**
     * 来自那个组的Excel配置
     */
    private String groupId;

    private Workbook workbook;

    public SheetHandlerInfo(Sheet sheet, int startRow, Class<?> tClass,Workbook workbook) {
        this.sheet = sheet;
        this.startRow = startRow;
        this.tClass = tClass;
        this.rowNum = sheet.getLastRowNum();
        this.workbook = workbook;
    }
}
