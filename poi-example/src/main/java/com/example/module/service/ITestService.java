package com.example.module.service;


import com.zhanghe.autoconfig.entity.SheetHandlerList;

import java.util.List;

public interface ITestService<T> {

    void save(List batch, Class<T>  c);

    void save(SheetHandlerList<T> sheetHandlerList,Class<T> c);
}
