package com.zhanghe.autoconfig.web.handler.params;


import com.zhanghe.autoconfig.entity.ExcelEntity;
import com.zhanghe.autoconfig.entity.ExcelGroupList;
import com.zhanghe.autoconfig.entity.ExcelGroupSheets;
import com.zhanghe.util.excel.mapper.ExcelMapper;
import com.zhanghe.util.excel.mapper.ExcelMappersEntity;
import com.zhanghe.util.excel.mapper.FileExcelMapperInfo;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: ZhangHe
 * @Date: 2020/5/27 17:27
 */
public class ExcelGroupListParamsConvert implements ExcelMethodParamsHandler  {
    @Override
    public boolean canConvert(MethodParameter methodParameter, Object value) {
        if(isExcelMappersEntity(value)  && isAssignable(methodParameter)){
            return true;
        }
        return false;
    }

    @Override
    public Object convert(MethodParameter methodParameter, Object value) {
        //获取excel 和 配置实体类
        ExcelMappersEntity excelMappersEntity = (ExcelMappersEntity) value;
        //获取实体类配置
        List<ExcelEntity> excelEntities = excelMappersEntity.getExcelEntities();
        //获取文件和excel映射信息
        List<FileExcelMapperInfo> fileExcelMapperInfos = excelMappersEntity.getExcelMappers();
        //储存集合
        ExcelGroupList excelGroupList = new ExcelGroupList();
        //遍历所有文件和excel映射信息
        for (FileExcelMapperInfo fileExcelMapperInfo : fileExcelMapperInfos) {
            //分别对每个文件的excelMapper转换为迭代器
            MultipartFile multipartFile = fileExcelMapperInfo.getMultipartFile();
            ExcelMapper[] excelMappers = fileExcelMapperInfo.getExcelMappers();
            for (ExcelMapper excelMapper : excelMappers) {
                ExcelGroupSheets excelGroupSheets = new ExcelGroupSheets(excelEntities,excelMapper);
                excelGroupSheets.setMultipartFile(multipartFile);
                excelGroupSheets.init();
                excelGroupList.add(excelGroupSheets);
            }
        }
        return excelGroupList;
    }

    @Override
    public boolean isAssignable(MethodParameter methodParameter) {
        return ClassUtils.isAssignable(ExcelGroupList.class,ResolvableType.forMethodParameter(methodParameter).getRawClass());
    }
}
