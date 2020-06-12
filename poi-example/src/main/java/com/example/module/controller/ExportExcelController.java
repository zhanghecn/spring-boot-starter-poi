package com.example.module.controller;

import com.example.module.model.dto.TestTypeExcelDTO;
import com.zhanghe.autoconfig.annotation.ExcelExport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("excel-export")
public class ExportExcelController {

    @ExcelExport(exportClass = TestTypeExcelDTO.class)
    @RequestMapping("/testType")
    public List<TestTypeExcelDTO> testType(){
        TestTypeExcelDTO testTypeExcelDTO = new TestTypeExcelDTO();
        testTypeExcelDTO.setDate(new Date());
        testTypeExcelDTO.setDecimal(new BigDecimal(99999));
        testTypeExcelDTO.setText("哈哈哈2");
        testTypeExcelDTO.setDecimal1(0.5f);
        testTypeExcelDTO.setDecimal2(555);

        List<TestTypeExcelDTO> list = new ArrayList<>();
        list.add(testTypeExcelDTO);

        return list;
    }
}
