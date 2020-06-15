package com.zhanghe.poi.util.excel.mapper;

import com.zhanghe.poi.autoconfig.entity.ExcelEntity;
import com.zhanghe.poi.autoconfig.entity.ExcelGroupSheets;
import com.zhanghe.poi.autoconfig.entity.SheetHandlerWrap;
import com.zhanghe.poi.util.excel.sheet.SheetHandlerAdapter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.poifs.crypt.temp.EncryptedTempData;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * excel转换实体类
 * 实体类转换excel
 * 中间映射使用类
 */
@Data
@Slf4j
public class ExcelMapper implements ExcelOperationMapper,Closeable{
    /**
     * POI工作簿
     */
    private Workbook workbook;

    /**
     * 文件流
     */
    private InputStream inputStream;

    /**
     * 对应的文件名称
     */
    private String fileName;

    public ExcelMapper(){
        try {
            this.workbook = WorkbookFactory.create(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ExcelMapper(Workbook workbook) {
        this.workbook = workbook;
    }

    /**
     * 通过流,创建
     * @param inputStream
     */
    public ExcelMapper(InputStream inputStream) {
        try {
            this.inputStream = inputStream;
            ZipSecureFile.setMinInflateRatio(0);
            Workbook sheets = WorkbookFactory.create(this.inputStream);
            this.workbook = sheets;
        } catch (IOException e) {
            Assert.isTrue(false,e.getMessage());
        }
    }

    /**
     * 把XSSF封装成SXSSF
     */
    public void warp(){
        if(this.workbook instanceof XSSFWorkbook) {
            this.workbook = new SXSSFWorkbook((XSSFWorkbook) workbook);
        }
    }
    @Override
    public List list(ExcelEntity excelXmlEntity){
        ExcelObjectMapperSuper excelObjectMapperSuper = new ExcelObjectMapperSuper(excelXmlEntity);
        return excelObjectMapperSuper.getListObject(workbook);
    }
    @Override
    public void writer(ExcelEntity excelXmlEntity, List<?> list, OutputStream outputStream){
        //封装成 sxssfWorkbook
        warp();
        getExcelByList(excelXmlEntity,list);
        writer(outputStream);
    }

    /**
     * 写入
     * @param outputStream
     */
    protected void writer(OutputStream outputStream){
        try {
            this.workbook.write(outputStream);
            log.info("导出完成->"+this.workbook.getAllNames());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 写入
     * @param zipOutStream
     */
    protected void writerZip(OutputStream zipOutStream){
        try {
            //加密的临时数据
            EncryptedTempData tempData = new EncryptedTempData();
            //获取输出流 ->输出到自己的位置
            OutputStream outputStream = tempData.getOutputStream();

            this.workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writer(List<ExcelEntity> excelEntity, ExcelGroupSheets excelGroupSheets, OutputStream outputStream) {
        //封装成 sxssfWorkbook
        warp();
        Map<Integer, List> sheets = excelGroupSheets.getSheets();
        for (ExcelEntity entity : excelEntity) {
            List list = sheets.get(entity.getSheetIndex());
            getExcelByList(entity, list);
        }
        //输出
        writer(outputStream);
    }

    private void getExcelByList(ExcelEntity entity, List list) {
        if(list!=null) {
            //获取全部sheet
            ExcelObjectMapperSuper excelObjectMapperSuper = new ExcelObjectMapperSuper(entity);
            excelObjectMapperSuper.getExcel(workbook, list);
        }
    }

    @Override
    public List list(ExcelEntity excelXmlEntity, Predicate<Sheet> sheetPredicate){
        ExcelObjectMapperSuper excelObjectMapperSuper = new ExcelObjectMapperSuper(excelXmlEntity);
        return excelObjectMapperSuper.getListObject(workbook,sheetPredicate);
    }
    @Override
    public SheetHandlerWrap sheetHandlerWrap(ExcelEntity excelXmlEntity){
        ExcelObjectMapperSuper excelObjectMapperSuper = new ExcelObjectMapperSuper(excelXmlEntity);
        return new SheetHandlerWrap(excelObjectMapperSuper.getSheetHandlerAdapter(this.workbook,fileName,inputStream, SheetHandlerAdapter.class));
    }

    public static ExcelMapper getExcelMapper(ExcelEntity load) throws IOException {
        if(!StringUtils.hasText(load.getTemplateName())){
            return new ExcelMapper();
        }
        String templateLocation = load.getTemplateLocation();
        String templateName = load.getTemplateName();
        String templateFile = templateLocation + "/" +templateName;
        ClassPathResource classPathResource = new ClassPathResource(templateFile);
        InputStream inputStream = classPathResource.getInputStream();
        ExcelMapper excelMapper = new ExcelMapper(inputStream);
        return excelMapper;
    }
    public static ExcelMapper getExcelMapper(List<ExcelEntity> loadGroups) throws IOException {
        if(loadGroups!=null&&!loadGroups.isEmpty()){
            ExcelEntity excelEntity = loadGroups.parallelStream().filter((x) -> StringUtils.hasText(x.getTemplateName())
                    && StringUtils.hasText(x.getTemplateLocation()))
                    .findAny().orElse(loadGroups.get(0));
            return getExcelMapper(excelEntity);
        }
        return null;
    }
    public static ExcelMapper[] getExcelMappers(String fileName, InputStream inputStream) throws IOException {
        //excel
        if(fileName.matches(".*\\.xlsx") || fileName.matches(".*\\.xls")){
            ExcelMapper excelMapper = new ExcelMapper(inputStream);
            excelMapper.setFileName(fileName);
            return new ExcelMapper[]{excelMapper};
        }else{ //zip文件
            //zip的输入流
            ZipInputStream zs = new ZipInputStream(inputStream, Charset.forName("GBK"));//解决中文文件夹乱码
            //封装成缓存输入流
            BufferedInputStream bs = new BufferedInputStream(zs);
            //每一个zip里面的文件
            ZipEntry ze;
            //获取的xls文件
            byte[] xlsFile = null;
            List<ExcelMapper> excelMappers = new ArrayList<>();
            while ((ze = zs.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    //
                } else if(ze.getName().matches(".*\\.xlsx")|| ze.getName().matches(".*\\.xls")){
                    long size = ze.getSize();
                    if (size > 0) {
                        xlsFile = new byte[(int) ze.getSize()];
                        bs.read(xlsFile, 0, (int) ze.getSize());
                        //封装成byte输入流
                        InputStream is = new ByteArrayInputStream(xlsFile);
                        ExcelMapper excelMapper = new ExcelMapper(is);
                        excelMapper.setFileName(ze.getName());
                        excelMappers.add(excelMapper);
                    }
                }
            }
            return excelMappers.toArray(new ExcelMapper[0]);
        }
    }

    @Override
    public void close() throws IOException {
        if(this.workbook!=null){
            this.workbook.close();
        }
    }
    public static void closeAll(ExcelMapper... excelMappers)  {
        if(excelMappers==null){
            return;
        }
        try {
            for (ExcelMapper excelMapper : excelMappers) {
                excelMapper.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
