package com.example.module.controller;

import com.example.module.model.dto.TestTypeExcelDTO;
import com.example.module.service.impl.TestTypeServiceImpl;
import com.zhanghe.autoconfig.annotation.ExcelExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("excel-export")
public class ExportExcelController {

    @Autowired
    TestTypeServiceImpl testTypeService;

    /**
     * 可自动转换数据的导出展示
     * @return
     *
     */
    @ExcelExport(exportClass = TestTypeExcelDTO.class)
    @RequestMapping("/testType")
    public List<TestTypeExcelDTO> testType(){
        List<TestTypeExcelDTO> testTypeExcelDTOS = testTypeService.exportList();
        return testTypeExcelDTOS;
    }


}
