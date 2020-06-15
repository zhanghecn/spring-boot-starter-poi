package com.zhanghe.poi.util.excel.mapper;

import com.zhanghe.poi.autoconfig.entity.ExcelEntity;
import com.zhanghe.poi.util.ExcelMapperUtil;
import com.zhanghe.poi.util.SpringContextHelper;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 储存实体类配置和对应的 文件和对应的excel映射器的信息
 * 用来取对应的excel映射器来操作转换实体类和导出实体类
 */
public class ExcelMappersEntity implements Closeable {
    private List<ExcelEntity> excelEntities;
    private List<FileExcelMapperInfo> excelMappers;
    private List<ExcelMapper> allExcelMappers;


    public ExcelMappersEntity(List<ExcelEntity> excelEntities) {
        this.excelEntities = excelEntities;
    }

    public ExcelEntity getExcelEntity() {
        return excelEntities.isEmpty()?null:excelEntities.get(0);
    }

    public List<ExcelEntity> getExcelEntities() {
        return excelEntities;
    }

    /**
     * 添加文件excel映射器
     * @param excelMapperArray
     * @param multipartFile
     */
    public void add(ExcelMapper[] excelMapperArray, MultipartFile multipartFile){
        if(this.excelMappers==null){
            this.excelMappers = new LinkedList();
        }
        if(this.allExcelMappers==null){
            this.allExcelMappers = new LinkedList();
        }
        this.excelMappers.add(new FileExcelMapperInfo(excelMapperArray,multipartFile));
        this.allExcelMappers.addAll(Arrays.asList(excelMapperArray));
    }
    public List<ExcelMapper> getAllExcelMappers(){
        return this.allExcelMappers;
    }

    public List<FileExcelMapperInfo> getExcelMappers() {
        return excelMappers;
    }


    @Override
    public void close() throws IOException {
        if(allExcelMappers!=null){
            for (ExcelMapper allExcelMapper : allExcelMappers) {
                allExcelMapper.close();
            }
        }
    }

  public static ExcelMappersEntity multipartFileMappersEntity(List<ExcelEntity> excelEntities,List<MultipartFile> multipartFiles){
        if(ObjectUtils.isEmpty(multipartFiles)){
           return null;
        }
      ExcelMappersEntity excelMappersEntity = new ExcelMappersEntity(excelEntities);

      ExcelMapperUtil excelMapperUtil = ExcelMapperUtil.getExcelMapperUtil(SpringContextHelper.applicationContext);

      try {
          for (MultipartFile multipartFile : multipartFiles) {
              excelMapperUtil.checkFile(multipartFile.getOriginalFilename());
              //获取封装excel映射
              ExcelMapper[] excelMapper = ExcelMapper.getExcelMappers(multipartFile.getOriginalFilename(),multipartFile.getInputStream());
              for (ExcelEntity load : excelEntities) {
                  //根据封装检查模板
                  excelMapperUtil.checkTemplate(excelMapper,load);
              }
              excelMappersEntity.add(excelMapper,multipartFile);
          }
      } catch (Exception e) {
          e.printStackTrace();
         throw new RuntimeException(e.getMessage());
      }
      return excelMappersEntity;
  }
}
