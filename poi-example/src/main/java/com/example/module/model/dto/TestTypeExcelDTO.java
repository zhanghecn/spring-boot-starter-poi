package com.example.module.model.dto;

import com.zhanghe.autoconfig.annotation.ExcelColumn;
import com.zhanghe.autoconfig.annotation.ExcelGroupEntity;
import com.zhanghe.util.excel.sheet.row.cell.property.DateCellConvert;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 基本配置
 */
@Data
@ExcelGroupEntity(notStandard = false,templateName = "test-type.xlsx",startRow = 1,uri = "/test/testType")
public class TestTypeExcelDTO {
    @ExcelColumn
    private String text;

    @ExcelColumn(1)
    private Integer number;

    @ExcelColumn(2)
    private float decimal1;

    @ExcelColumn(3)
    private double decimal2;

    @ExcelColumn(4)
    private BigDecimal decimal;

    @ExcelColumn(value = 5,dateFormat = DateCellConvert.DATE)
    private Date  date;
}
