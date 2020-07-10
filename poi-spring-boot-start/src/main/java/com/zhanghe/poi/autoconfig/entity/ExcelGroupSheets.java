package com.zhanghe.poi.autoconfig.entity;

import com.zhanghe.poi.util.excel.mapper.ExcelMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: ZhangHe
 * @since: 2020/5/27 17:11
 */
@Data
@NoArgsConstructor
public class ExcelGroupSheets {
    /**
     * key 是 sheet下标
     * value 是转换后的对象集合
     */
    private Map<Integer,List> sheets;

    private List<ExcelEntity> excelEntities;

    private ExcelMapper excelMapper;

    private String groupId;

    private String fileName;

    private MultipartFile multipartFile;

    public ExcelGroupSheets(List<ExcelEntity> excelEntities, ExcelMapper excelMapper) {
        this.excelEntities = excelEntities;
        this.excelMapper = excelMapper;
    }

    public ExcelGroupSheets(String fileName) {
        this.fileName = fileName;
    }

    public void init(){
        Map<Integer,List> map = new HashMap<>();
        List<ExcelEntity> excelEntities = this.excelEntities;
        if(excelEntities!=null && !excelEntities.isEmpty()){
            if(StringUtils.isEmpty(this.groupId)){
                this.groupId = excelEntities.get(0).getGroupId();
            }
            for (ExcelEntity excelEntity : excelEntities) {
                List list = this.excelMapper.list(excelEntity);
                map.put(excelEntity.getSheetIndex(),list);
            }
        }
        this.sheets = map;
    }

    public void putSheetValue(int sheetIndex,Object o){
        ArrayList   arrayList = new ArrayList();
        arrayList.add(o);
        putSheetValues(sheetIndex,arrayList);
    }

    public void putSheetValues(int sheetIndex,List o){
        if(this.sheets==null){
            this.sheets = new HashMap<>();
        }
        this.sheets.put(sheetIndex,o);
    }

    public <T> T getOne(int sheetIndex,Class<T> c){

        return (T) getOne(sheetIndex);
    }

    public Object  getOne(int sheetIndex){
        List list = sheets.get(sheetIndex);
        if(list!=null&&!list.isEmpty()){
            Object o =  list.get(0);
            return o;
        }
        return null;
    }

    public  List<Object> getList(int sheetIndex){
        List list = sheets.get(sheetIndex);
        return list;
    }
    public <T> List<T> getList(int sheetIndex,Class<T> c){
        return (List<T>) getList(sheetIndex);
    }
}
