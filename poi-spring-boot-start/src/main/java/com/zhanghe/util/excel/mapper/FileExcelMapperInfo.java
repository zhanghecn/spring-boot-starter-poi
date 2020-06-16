package com.zhanghe.util.excel.mapper;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件和对应的excel映射器的信息
 * 这个文件中用来操作excel转换工具类
 * @Author: ZhangHe
 * @Date: 2020/4/22 15:52
 */
public class FileExcelMapperInfo {
    /**
     * excel映射器 用来操作实体类和excel中间类
     */
    private   ExcelMapper[] excelMappers;
    /**
     * excel 来自的文件
     */
    private MultipartFile multipartFile;

    public FileExcelMapperInfo(ExcelMapper[] excelMappers, MultipartFile multipartFile) {
        this.excelMappers = excelMappers;
        this.multipartFile = multipartFile;
    }

    public ExcelMapper[] getExcelMappers() {
        return excelMappers;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }
}
