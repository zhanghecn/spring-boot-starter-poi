package com.zhanghe.poi.util.excel.mapper;

import com.zhanghe.poi.autoconfig.entity.ExcelEntity;
import com.zhanghe.poi.autoconfig.entity.ExcelGroupSheets;
import com.zhanghe.poi.autoconfig.entity.SheetHandlerWrap;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.OutputStream;
import java.util.List;
import java.util.function.Predicate;

/**
 * 操作Excel 映射的方法
 * @Author: ZhangHe
 * @Date: 2020/4/22 16:33
 */
public interface ExcelOperationMapper {
    /**
     * 通过配置转换实体类
     * @param excelEntity 实体类配置
     * @return
     */
    List list(ExcelEntity excelEntity);


    /**
     * 通过实体类配置 和 对应数据 输出到输出流上
     * @param excelEntity  实体类配置
     * @param list          数据
     * @param outputStream  输出位置
     */
    void writer(ExcelEntity excelEntity, List<?> list, OutputStream outputStream);

    /**
     * 通过实体类配置 和 对应数据 输出到输出流上
     * @param excelEntity  实体类配置
     * @param excelGroupSheets          数据
     * @param outputStream  输出位置
     */
    void writer(List<ExcelEntity> excelEntity, ExcelGroupSheets excelGroupSheets, OutputStream outputStream);

    /**
     * 通过实体类配置 和 对应筛选条件 进行筛选获取
     * @param excelEntity 实体类配置
     * @param sheetPredicate 筛选条件
     * @return
     */
    List list(ExcelEntity excelEntity, Predicate<Sheet> sheetPredicate);

    /**
     * 通过配置转换sheet封装类
     * @param excelEntity
     * @return
     */
    SheetHandlerWrap sheetHandlerWrap(ExcelEntity excelEntity);
}
