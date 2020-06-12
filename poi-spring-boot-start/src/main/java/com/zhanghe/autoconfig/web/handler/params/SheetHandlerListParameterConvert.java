package com.zhanghe.autoconfig.web.handler.params;


import com.zhanghe.autoconfig.entity.ExcelEntity;
import com.zhanghe.autoconfig.entity.SheetHandlerList;
import com.zhanghe.autoconfig.entity.SheetHandlerWrap;
import com.zhanghe.util.excel.mapper.ExcelMapper;
import com.zhanghe.util.excel.mapper.ExcelMappersEntity;
import com.zhanghe.util.excel.mapper.FileExcelMapperInfo;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class SheetHandlerListParameterConvert implements ExcelMethodParamsHandler {

    @Override
    public boolean canConvert(MethodParameter methodParameter, Object value) {
        Class<?> parameterType = methodParameter.getParameterType();
        if(isExcelMappersEntity(value)  && ClassUtils.isAssignable(SheetHandlerList.class,parameterType)){
            return true;
        }
        return false;
    }

    @Override
    public Object convert(MethodParameter methodParameter, Object value) {
        //获取excel 和 配置实体类
        ExcelMappersEntity excelMappersEntity = (ExcelMappersEntity) value;
        //获取实体类配置
        ExcelEntity excelEntity = excelMappersEntity.getExcelEntity();
        //获取文件和excel映射信息
        List<FileExcelMapperInfo> fileExcelMapperInfos = excelMappersEntity.getExcelMappers();
        //储存wraps集合
        SheetHandlerList sheetHandlerWraps = new SheetHandlerList();
        //遍历所有文件和excel映射信息
        for (FileExcelMapperInfo fileExcelMapperInfo : fileExcelMapperInfos) {
            //分别对每个文件的excelMapper转换为迭代器
            MultipartFile multipartFile = fileExcelMapperInfo.getMultipartFile();
            ExcelMapper[] excelMappers = fileExcelMapperInfo.getExcelMappers();
            for (ExcelMapper excelMapper : excelMappers) {
                SheetHandlerWrap sheetHandlerWrap = excelMapper.sheetHandlerWrap(excelEntity);
                sheetHandlerWrap.setFile(multipartFile);
                sheetHandlerWraps.add(sheetHandlerWrap);
            }
        }
        return sheetHandlerWraps;
    }
}
