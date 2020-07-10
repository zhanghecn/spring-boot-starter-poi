package com.zhanghe.poi.util.excel.mapper;


import com.zhanghe.poi.autoconfig.entity.ExcelEntity;
import com.zhanghe.poi.util.excel.sheet.SheetHandler;
import com.zhanghe.poi.util.excel.sheet.SheetHandlerAdapter;
import com.zhanghe.poi.util.excel.sheet.SheetHandlerInfo;
import com.zhanghe.poi.util.excel.sheet.SheetNotStandardHandlerAdapter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.io.InputStream;
import java.lang.reflect.Constructor;

/**
 * excel实体配置 操作 映射的抽象辅助类
 * @author: ZhangHe
 * @since: 2020/4/22 16:06
 */
public abstract class AbstractExcelEntityObjectMapper implements ExcelObjectMapper {
    /**
     * 用作操作辅助中的方法需要用的实体类的配置信息
     */
    protected ExcelEntity excelEntity;

    public AbstractExcelEntityObjectMapper(ExcelEntity excelXmlEntity) {
        this.excelEntity = excelXmlEntity;
    }

    /**
     * 获取sheet页的操作对象
     * @param workbook poi workbook
     * @param sheetHand sheetHand
     * @param <T> sheetHand
     * @return T
     */
    protected <T> T getSheetHandlerAdapter(Workbook workbook, Class<T> sheetHand){
        return getSheetHandlerAdapter(workbook,null,null,sheetHand);
    }

    /**
     * 通过是否标准sheet页确定处理适配器的类
     * @return SheetHandler
     */
    protected Class<? extends SheetHandler> getAutoHandlerAdapterClass(){
        Class<? extends SheetHandler> handClass;
        if(ClassUtils.isAssignable(SheetHandler.class,this.excelEntity.getClazz())){
            handClass = this.excelEntity.getClazz();
        }else{
            if(this.excelEntity.isNotStandard()){
                handClass = SheetNotStandardHandlerAdapter.class;
            }else{
                handClass= SheetHandlerAdapter.class;
            }
        }
        return handClass;
    }

    /**
     * 获取sheet页的操作对象
     * @param workbook workbook
     * @param fileName fileName
     * @param inputStream inputStream
     * @param sheetHand sheetHand
     * @param <T> T
     * @return T
     */
    protected <T> T getSheetHandlerAdapter(Workbook workbook, String fileName, InputStream inputStream, Class<T> sheetHand)  {

            SheetHandlerInfo sheetHandlerInfo = getSheetHandlerInfo(workbook);
            sheetHandlerInfo.setFileName(fileName);
            sheetHandlerInfo.setInputStream(inputStream);
        return getSheetHandlerAdapter(sheetHandlerInfo,sheetHand);
    }

    protected <T> T getSheetHandlerAdapter(SheetHandlerInfo sheetHandlerInfo, Class<T> sheetHand)  {
        T sheetHandler = null;
        try {
            Constructor<? extends T> constructor = ReflectionUtils.accessibleConstructor(sheetHand, SheetHandlerInfo.class);
            sheetHandler = BeanUtils.instantiateClass(constructor, sheetHandlerInfo);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return sheetHandler;
    }
    private SheetHandlerInfo getSheetHandlerInfo(Workbook workbook) {
        //获取第几个sheet页
        int sheetIndex = excelEntity.getSheetIndex();
        //获取开始行数
        int startRow = excelEntity.getStartRow();
        Class clazz = excelEntity.getClazz();
        Sheet sheetAt = workbook.getSheetAt(sheetIndex);
        return getSheetHandlerInfo(startRow, clazz, sheetAt, workbook);
    }

    /*
     * 获取当前sheet页的信息
     */
    protected SheetHandlerInfo getSheetHandlerInfo(int startRow, Class<?> clazz, Sheet sheetAt,Workbook workbook) {
        SheetHandlerInfo sheetHandlerInfo = new SheetHandlerInfo(sheetAt, startRow, clazz,workbook);
        //拷贝不重要的属性
        BeanUtils.copyProperties(excelEntity,sheetHandlerInfo);
        return sheetHandlerInfo;
    }
    /**
     * 比对模板是否一样
     * @param template 自己的模板
     * @param sheetHandlerInfo1 对方的sheet信息
     * @return boolean
     */
    public boolean comparison(Workbook template, SheetHandlerInfo sheetHandlerInfo1){
        //获取第几个sheet页
        SheetHandlerInfo sheetHandlerInfo = getSheetHandlerInfo(template);
        //对方的
        SheetHandlerAdapter sheetHandlerAdapter = new SheetHandlerAdapter(sheetHandlerInfo);
        //比较
        return sheetHandlerAdapter.comparison(sheetHandlerInfo1);
    }
}
