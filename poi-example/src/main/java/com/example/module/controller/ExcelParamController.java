package com.example.module.controller;

import com.example.module.model.dto.TestTypeExcelDTO;
import com.example.module.model.vo.ResultMap;
import com.example.module.service.impl.TestTypeServiceImpl;
import com.zhanghe.autoconfig.annotation.ExcelParam;
import com.zhanghe.autoconfig.entity.SheetHandlerList;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("excel-file")
@AllArgsConstructor
public class ExcelParamController {

    TestTypeServiceImpl testTypeService;


    @PostMapping("/testType")
    public ResultMap testType(@ExcelParam List<TestTypeExcelDTO> testTypeExcelDTOS){
        testTypeService.save(testTypeExcelDTOS);
        return ResultMap.ok(testTypeExcelDTOS);
    }

    @PostMapping("/testType2")
    public ResultMap testType2(@ExcelParam SheetHandlerList<TestTypeExcelDTO> testTypeExcelDTOS){
        testTypeService.save(testTypeExcelDTOS);
        return ResultMap.ok();
    }

}
