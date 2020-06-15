package com.zhanghe.poi.autoconfig.entity;

import com.zhanghe.poi.autoconfig.entity.extend.AbstractExcelConfiguration;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * excel配置
 * 请使用：/excel/import/{configName}
 * 文件key:excelFile
 *
 */
@Data
@SuperBuilder(toBuilder = true)
public class ExcelEntity extends AbstractExcelConfiguration implements Serializable{
    //开始行号
    private int startRow;
    //sheet的位置
    private int sheetIndex;
    //1.取自动样式的行(noAutoStyle=true的时候失效) TODO:2.需要使用扩展头部信息的位置 3.autoCheckHeader 自动检查头部的位置
    private int headerRow;
    //1.取自动样式的列(noAutoStyle=true的时候失效) TODO:2.autoCheckHeader 自动检查头部的位置
    private int headerStartCol;
    //自动比较头部
    private boolean autoCheckHeader = true;
    /**
     * 不是一个标准模板？
     */
    private boolean notStandard;
    /**
     * 用作没有模板读取或者导出的时候指定最多列数量
     */
    private int maxColumnNum;

    /**
     * 分组
     */
    private String groupId;
}
