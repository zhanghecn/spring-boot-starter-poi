package com.zhanghe.poi.util;


import com.zhanghe.poi.autoconfig.entity.ExcelEntity;
import com.zhanghe.poi.autoconfig.entity.SheetHandlerWrap;
import com.zhanghe.poi.exception.ExcelException;
import com.zhanghe.poi.util.excel.mapper.ExcelMapper;
import com.zhanghe.poi.util.excel.mapper.ExcelObjectMapper;
import com.zhanghe.poi.util.excel.mapper.ExcelObjectMapperSuper;
import com.zhanghe.poi.util.excel.sheet.SheetHandlerInfo;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

public interface ExcelMapperUtil {
    //加载模板实体类
    ExcelEntity load(String configName);

    //加载模板实体类
    List<ExcelEntity> loadGroups(String groupId);

    List<ExcelEntity> loadGroups(Class<?> c);

    /**
     * 得到其中存在的uri
     * @param excelEntities 配置实体信息
     * @return String
     */
    static String getURI(List<ExcelEntity> excelEntities){
        if(excelEntities!=null && !excelEntities.isEmpty()){
          return   excelEntities.parallelStream()
                  .map((excelEntity -> excelEntity.getUri()))
                  .filter(x-> StringUtils.hasText(x)).findAny().orElseGet(()->null);
        }
        return null;
    }

    //检查模板
    static boolean isTemplate(ExcelMapper[] excelMapper, ExcelEntity load) throws IOException {
        if( load.isAutoCheckHeader()) {
            ExcelObjectMapper excelObjectMapperSuper = new ExcelObjectMapperSuper(load);
            ClassPathResource classPathResource = new ClassPathResource(load.getTemplateLocation() + "/" + load.getTemplateName());
            Workbook template = WorkbookFactory.create(classPathResource.getInputStream());
            for (ExcelMapper mapper : excelMapper) {
                SheetHandlerWrap sheetHandlerWrap = mapper.sheetHandlerWrap(load);
                SheetHandlerInfo sheetHandlerInfo = sheetHandlerWrap.getSheetHandlerInfo();
                boolean comparison = excelObjectMapperSuper.comparison(template, sheetHandlerInfo);
                if(!comparison){
                    return false;
                }
            }
        }
        return true;
    }
    //检查模板
    default void checkTemplate(ExcelMapper[] excelMapper, ExcelEntity load) throws IOException {
           if (!isTemplate(excelMapper,load)) {
               throw new ExcelException("模板不一致。");
           }
    }
    default void checkFile(String fileName) {
        if(!isExcelFile(fileName)){
            throw new ExcelException("文件格式不是xlsx,xls或者zip格式");
        }
    }
    static boolean isExcelFile(String fileName) {
        boolean b=fileName.matches(".*\\.xlsx") || fileName.matches(".*\\.xls")||fileName.matches(".*\\.zip");
        return b;
    }
    static ExcelMapperUtil getExcelMapperUtil(ApplicationContext applicationContext) {
        return applicationContext.getBean(ExcelMapperUtil.class);
    }
}
