package com.example.module.controller;

import com.example.module.model.dto.TestTypeExcelDTO;
import com.example.module.model.vo.ResultMap;
import com.zhanghe.autoconfig.annotation.ExcelParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("excel-file")
public class ExcelController {


    @PostMapping("/testType")
    public ResultMap testType(@ExcelParam List<TestTypeExcelDTO> testTypeExcelDTOS){
        return ResultMap.ok(testTypeExcelDTOS);
    }


}
