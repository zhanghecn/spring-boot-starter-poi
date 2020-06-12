package com.example.module.controller;

import com.example.module.model.dto.TestTypeExcelDTO;
import com.example.module.model.vo.ResultMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 请查看poi-spring-boot-start 的 ExcelController
 * 导入都是固定api + 动态groupdId的
 * 默认是@ExcelGroupEntity 添加注解的类名
 * 路径如下： /excel/import/{configName}
 * 并且提交表单的excel文件参数名称必须叫：excelFile
 */
@RestController
@RequestMapping("test")
public class ExcelDataController {

    /**
     * 访问 /excel/import/testTypeExcelDTO 来进行映射到这个接口
     * @param testTypeExcelDTOS
     * @param otherParam
     * @return
     */
    @PostMapping("/testType")
    public ResultMap testType(List<TestTypeExcelDTO> testTypeExcelDTOS, String otherParam){
        return ResultMap.ok().add("data",testTypeExcelDTOS).add("params",otherParam);
    }
}
