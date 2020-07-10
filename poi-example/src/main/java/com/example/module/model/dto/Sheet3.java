package com.example.module.model.dto;

import com.zhanghe.poi.autoconfig.annotation.ExcelGroupEntity;
import com.zhanghe.poi.util.excel.sheet.AbstractSheetHandler;
import com.zhanghe.poi.util.excel.sheet.SheetHandlerInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author: ZhangHe
 * @since: 2020/7/10 14:46
 */
//不规则的sheet  notStandard 一定要是true  当前sheetIndex是2
@ExcelGroupEntity(groupId = "manySheet",notStandard = true,templateName = "多sheet展示.xlsx",startRow = 1,noAutoStyle = true,sheetIndex = 2)
public class Sheet3 extends AbstractSheetHandler {

    //这个会被自动传入
    public Sheet3(SheetHandlerInfo sheetInfo) {
        super(sheetInfo);
    }

    /**
     * 半自动原生获取
     * @param condition 条件
     * @return
     */
    @Override
    public List getObjects(Predicate<Sheet> condition) {
        //这个是xssfsheet
        XSSFSheet xssfSheet = xssfSheet();

        //关于原生值请自己获取
        //这边只是做个模拟
        return Arrays.asList(Collections.singletonList("1"),Collections.singletonList("2"));
    }

    /**
     * 半自动原生写入
     * @param list 写入的数据
     */
    @Override
    public void write(List<?> list) {
        //可以看到传递过来要写的值
        System.out.println(list);

        //我就不用传过来的list写了,直接模拟死数据
        Sheet sheet = sheetInfo.getSheet();
        for (int i = 0; i < 10; i++) {
            int rowIndex = i + 7;
            Row row = sheet.createRow(rowIndex);
            Cell cell = row.createCell(1);
            cell.setCellValue("张三"+list+i);
            Cell cell2 = row.createCell(7);
            cell2.setCellValue("李四"+list+i);
        }
    }
}
