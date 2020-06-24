package com.example.module.controller;

import com.example.module.model.TestTypeEntity;
import com.example.module.model.dto.TestTypeExcelDTO;
import com.example.module.model.dto.TestTypeExcelDTO2;
import com.example.module.model.vo.ResultMap;
import com.example.module.service.impl.TestTypeServiceImpl;
import com.zhanghe.autoconfig.entity.SheetHandlerList;
import com.zhanghe.autoconfig.entity.SheetHandlerWrap;
import com.zhanghe.util.DOUtils;
import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//TODO: 该自动API 是为了提供前端共用组件封装  请查看vue项目
/**
 * 请查看poi-spring-boot-start 的 ExcelController
 * 导入都是固定api + 动态groupdId的
 * 默认是@ExcelGroupEntity 添加注解的类名
 * 路径如下： /excel/import/{configName}
 * 并且提交表单的excel文件参数名称必须叫：excelFile
 */
@RestController
@RequestMapping("auto-api")
@AllArgsConstructor
public class AutoApiController {

    TestTypeServiceImpl testTypeService;
    /**
     * 访问 /excel/import/testTypeExcelDTO 来进行映射到这个接口
     * @param testTypeExcelDTOS
     * @param otherParam
     *
     */
    @PostMapping("/testType")
    public ResultMap testType(List<TestTypeExcelDTO> testTypeExcelDTOS, String otherParam){
        testTypeService.save(testTypeExcelDTOS);
        return ResultMap.ok().add("data",testTypeExcelDTOS).add("params",otherParam);
    }

    /**
     * 访问 /excel/import/testTypeExcelDTO2 TODO:是testTypeExcelDTO2 多了个2
     * 对应映射 TestTypeExcelDTO2
     * @param testTypeExcelDTOS
     * @param otherParam
     *
     */
    @PostMapping("/testType2")
    public ResultMap testType2(SheetHandlerList<TestTypeExcelDTO2> testTypeExcelDTOS, String otherParam){
        for (SheetHandlerWrap<TestTypeExcelDTO2> testTypeExcelDTO : testTypeExcelDTOS) {
            for (TestTypeExcelDTO2 testTypeExcelDTO2 : testTypeExcelDTO) {
                //这个是判断是，可能读取中存在中间某部分是空的情况，您可以选择是break还是continue
                if(ObjectUtils.isEmpty(testTypeExcelDTO2)){
                    break;
                }
                TestTypeEntity testTypeEntity = DOUtils.copyProperties(testTypeExcelDTO2, TestTypeEntity.class);
                System.out.println(testTypeEntity);
            }
        }
        return ResultMap.ok().add("params",otherParam);
    }
}
