package com.example.module.controller;

import com.example.module.model.dto.*;
import com.example.module.model.vo.ResultMap;
import com.example.module.service.impl.TestTypeServiceImpl;
import com.zhanghe.poi.autoconfig.annotation.ExcelParam;
import com.zhanghe.poi.autoconfig.entity.ExcelGroupList;
import com.zhanghe.poi.autoconfig.entity.ExcelGroupSheets;
import com.zhanghe.poi.autoconfig.entity.SheetHandlerList;
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

    @PostMapping("/testMerge")
    public ResultMap testMerge(@ExcelParam List<TestMergeDTO> testMergeDTOS){
        System.out.println(testMergeDTOS);
        return ResultMap.ok();
    }

    @PostMapping("/testManySheet")
    public ResultMap testManySheet(@ExcelParam("manySheet") ExcelGroupList excelGroupSheets){
        //可能zip里面有多个excel
        for (ExcelGroupSheets excelGroupSheet : excelGroupSheets) {
            List<Sheet1> list1 = excelGroupSheet.getList(0, Sheet1.class);
            //由于整个sheet是固定的所以就一个
            Sheet2 list2 = excelGroupSheet.getOne(1, Sheet2.class);
            List<Sheet3> list3 = excelGroupSheet.getList(2, Sheet3.class);
            System.out.println(list2);
        }
        return ResultMap.ok();
    }
}
