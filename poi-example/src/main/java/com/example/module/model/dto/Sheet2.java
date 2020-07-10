package com.example.module.model.dto;

import com.zhanghe.poi.autoconfig.annotation.ExcelColumn;
import com.zhanghe.poi.autoconfig.annotation.ExcelGroupEntity;
import com.zhanghe.poi.autoconfig.annotation.ExcelRow;
import com.zhanghe.poi.autoconfig.annotation.type.ImageType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: ZhangHe
 * @since: 2020/7/10 14:46
 */
@Data
//不规则的sheet  notStandard 一定要是true  当前sheetIndex是1
@ExcelGroupEntity(groupId = "manySheet",notStandard = true,templateName = "多sheet展示.xlsx",startRow = 1,noAutoStyle = true,sheetIndex = 1)
public class Sheet2 {
    //绩效
    @ExcelColumn(1)
    @ExcelRow(5)
    private Double performance;

    //合同值
    @ExcelColumn(1)
    @ExcelRow(6)
    private BigDecimal geTongZhi;

    //份额
    @ExcelColumn(1)
    @ExcelRow(7)
    private String quotient;

    //图片
    @ExcelRow(13)
    @ExcelColumn(1)
    private ImageType imageType;
}
