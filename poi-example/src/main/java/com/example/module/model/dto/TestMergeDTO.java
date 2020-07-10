package com.example.module.model.dto;

import com.zhanghe.poi.autoconfig.annotation.ExcelColumn;
import com.zhanghe.poi.autoconfig.annotation.ExcelGroupEntity;
import com.zhanghe.poi.autoconfig.annotation.style.ColumnStyle;
import lombok.Data;

/**
 * @author: ZhangHe
 * @since: 2020/7/7 14:11
 */
@Data
@ExcelGroupEntity(notStandard = false,templateName = "合并单元格.xlsx",startRow = 1,noAutoStyle = true)
public class TestMergeDTO {
    //一级目录
    @ExcelColumn
    private String oneLevel;

    //二级目录
    @ExcelColumn(1)
    private String twoLevel;

    //三级目录
    @ExcelColumn(2)
    private String threeLevel;

    //都对一列的样式
    @ExcelColumn
    private ColumnStyle columnStyle1;

    //对第二列的样式
    @ExcelColumn(1)
    private ColumnStyle columnStyle2;

    //对第三列的样式
    @ExcelColumn(2)
    private ColumnStyle columnStyle3;
}
