package com.zhanghe.poi.autoconfig.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ExcelObjectData<T> {
   private MultipartFile multipartFile;

   private List<T> list;

    public ExcelObjectData(MultipartFile multipartFile, List<T> list) {
        this.multipartFile = multipartFile;
        this.list = list;
    }
}
