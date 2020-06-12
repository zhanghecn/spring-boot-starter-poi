package com.example.module.service.impl;

import com.example.module.model.dto.TestTypeExcelDTO;
import com.example.module.service.ITestService;
import com.zhanghe.autoconfig.entity.SheetHandlerList;
import com.zhanghe.autoconfig.entity.SheetHandlerWrap;

import java.util.List;

public class TestServiceImpl implements ITestService<TestTypeExcelDTO> {


    @Override
    public void save(List batch, Class<TestTypeExcelDTO> c) {

    }

    @Override
    public void save(SheetHandlerList<TestTypeExcelDTO> sheetHandlerList, Class<TestTypeExcelDTO> c) {
        for (SheetHandlerWrap<TestTypeExcelDTO> testTypeExcelDTOS : sheetHandlerList) {

        }
    }
}
