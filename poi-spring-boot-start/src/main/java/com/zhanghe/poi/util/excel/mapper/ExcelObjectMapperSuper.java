package com.zhanghe.poi.util.excel.mapper;


import com.zhanghe.poi.autoconfig.entity.ExcelEntity;
import com.zhanghe.poi.util.excel.sheet.SheetHandler;
import com.zhanghe.poi.util.excel.sheet.SheetHandlerInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Predicate;

/**
 * 用作操作对象和excel 转换类
 */
@Slf4j
public class ExcelObjectMapperSuper extends AbstractExcelEntityObjectMapper {


    public ExcelObjectMapperSuper(ExcelEntity excelXmlEntity) {
        super(excelXmlEntity);
    }

    public List getListObject(Workbook workbook, Predicate<Sheet> sheetPredicate){
        SheetHandler sheetHandlerAdapter = getSheetHandlerAdapter(workbook, getAutoHandlerAdapterClass());
        return sheetHandlerAdapter.getObjects(sheetPredicate);
    }



    /**
     * 获取excel
     * @param template poi workbook
     */
    @Override
    public void getExcel(Workbook template,List<?> list) {
        //获取第几个sheet页
        int sheetIndex = excelEntity.getSheetIndex();
        //获取开始行数
        int startRow = excelEntity.getStartRow();
        if(list!=null&&list.size()>0){
            Class<?> clazz = excelEntity.getClazz();
            Sheet sheetAt = getSheet(template,sheetIndex);
            SheetHandlerInfo sheetHandlerInfo = getSheetHandlerInfo(startRow, clazz, sheetAt,template);
            SheetHandler sheetHandlerAdapter = getSheetHandlerAdapter(sheetHandlerInfo, getAutoHandlerAdapterClass());
            //写入到该页
            sheetHandlerAdapter.write(list);
            log.debug(String.format("第%s页sheet已经写入了:"+list.size()+"个对象",sheetIndex+1));
        }
    }

    /**
     * 通过是否存在模板名称，确定是否需要创建sheet获取
     * @param template poi workbook
     * @param sheetIndex sheet下标
     *
     */
    private Sheet getSheet(Workbook template, int sheetIndex) {
        Sheet sheet;
        if(!StringUtils.hasText(super.excelEntity.getTemplateName())){
             sheet = template.createSheet();
        }else{
            sheet = template.getSheetAt(sheetIndex);
        }
        return sheet;
    }


}
