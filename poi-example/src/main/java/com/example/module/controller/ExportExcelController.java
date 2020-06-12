package com.example.module.controller;

import com.example.module.model.dto.TestTypeExcelDTO;
import com.zhanghe.autoconfig.annotation.ExcelExport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("excel-export")
public class ExportExcelController {

    @ExcelExport(exportClass = TestTypeExcelDTO.class)
    @PostMapping("/testType")
    public List<TestTypeExcelDTO> testType(){
        TestTypeExcelDTO testTypeExcelDTO = new TestTypeExcelDTO();
        testTypeExcelDTO.setDate(new Date());

        List<TestTypeExcelDTO> list = new ArrayList<>();
        list.add(testTypeExcelDTO);

        return list;
    }
}
