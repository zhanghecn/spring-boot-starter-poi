package com.example.module.service;


import com.zhanghe.autoconfig.entity.SheetHandlerList;

import java.util.List;

public interface ITestService<T> {

    void save(List<T> batch);

    void save(SheetHandlerList<T> sheetHandlerList);
}
