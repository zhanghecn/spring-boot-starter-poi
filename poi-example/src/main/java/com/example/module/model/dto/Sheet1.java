package com.example.module.model.dto;

import com.zhanghe.poi.autoconfig.annotation.ExcelColumn;
import com.zhanghe.poi.autoconfig.annotation.ExcelGroupEntity;
import lombok.Data;

/**
 * @author: ZhangHe
 * @since: 2020/7/10 14:46
 */
@Data
//记住多个sheet的groupId一定要一致
@ExcelGroupEntity(groupId = "manySheet",notStandard = false,templateName = "多sheet展示.xlsx",startRow = 1,noAutoStyle = true,sheetIndex = 0)
public class Sheet1 {
    @ExcelColumn(0)
    private String header1;

    @ExcelColumn(1)
    private String header2;

    @ExcelColumn(2)
    private String header3;

    public Sheet1(String header1, String header2, String header3) {
        this.header1 = header1;
        this.header2 = header2;
        this.header3 = header3;
    }
}
