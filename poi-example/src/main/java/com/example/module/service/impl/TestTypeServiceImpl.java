package com.example.module.service.impl;

import com.example.module.model.TestTypeEntity;
import com.example.module.model.dto.TestTypeExcelDTO;
import com.example.module.service.ITestService;
import com.zhanghe.autoconfig.entity.ExcelGroupSheets;
import com.zhanghe.autoconfig.entity.SheetHandlerList;
import com.zhanghe.autoconfig.entity.SheetHandlerWrap;
import com.zhanghe.util.DOUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TestTypeServiceImpl implements ITestService<TestTypeExcelDTO> {


    @Override
    public void save(List<TestTypeExcelDTO> batch) {
        List<TestTypeEntity> testTypeEntities = DOUtils.copyList(batch, TestTypeEntity.class);
        System.out.println("保存了："+testTypeEntities);
    }

    @Override
    public void save(SheetHandlerList<TestTypeExcelDTO> sheetHandlerList) {
        for (SheetHandlerWrap<TestTypeExcelDTO> testTypeExcelDTOS : sheetHandlerList) {
            List list = new ArrayList();
            for (TestTypeExcelDTO testTypeExcelDTO : testTypeExcelDTOS) {
                if(testTypeExcelDTO==null){
                    break;
                }
                System.out.println(testTypeExcelDTO);
                TestTypeEntity testTypeEntity = DOUtils.copyProperties(testTypeExcelDTO, TestTypeEntity.class);
                list.add(testTypeEntity);
            }
            System.out.printf("add:"+list);
        }
    }

    @Override
    public List<TestTypeExcelDTO> exportList() {
        //第一行数据
        TestTypeEntity testTypeEntity1 =
                new TestTypeEntity("第一行",
                        1,2.0f,
                        3.0,
                        new BigDecimal(99999),new Date());

        //第二行数据
        TestTypeEntity testTypeEntity2 =
                new TestTypeEntity("第二行",
                        3,2.0f,
                        3.0,
                        new BigDecimal(99999),new Date());

        //第三行数据
        TestTypeEntity testTypeEntity3 =
                new TestTypeEntity("第二行",
                        3,2.0f,
                        4.0,
                        new BigDecimal(99299),new Date());

        //假设从数据查出来的
        List<TestTypeEntity> collect = Stream.of(testTypeEntity1, testTypeEntity2, testTypeEntity3)
                .collect(Collectors.toList());
        //转换成导出的dto
        List<TestTypeExcelDTO> testTypeExcelDTOS = DOUtils.copyList(collect, TestTypeExcelDTO.class);
        return testTypeExcelDTOS;
    }

    @Override
    public ExcelGroupSheets exportSheets() {
        return null;
    }

    @Override
    public List<ExcelGroupSheets> exportZip() {
        return null;
    }
}
