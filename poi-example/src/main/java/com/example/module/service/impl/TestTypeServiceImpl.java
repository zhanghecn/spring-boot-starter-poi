package com.example.module.service.impl;

import com.example.module.model.TestTypeEntity;
import com.example.module.model.dto.TestTypeExcelDTO;
import com.example.module.service.ITestService;
import com.zhanghe.autoconfig.entity.SheetHandlerList;
import com.zhanghe.autoconfig.entity.SheetHandlerWrap;
import com.zhanghe.util.DOUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
}
