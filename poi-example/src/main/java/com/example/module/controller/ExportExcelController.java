package com.example.module.controller;

import com.example.module.model.dto.Sheet1;
import com.example.module.model.dto.Sheet2;
import com.example.module.model.dto.TestMergeDTO;
import com.example.module.model.dto.TestTypeExcelDTO;
import com.example.module.service.impl.TestTypeServiceImpl;
import com.zhanghe.poi.autoconfig.annotation.ExcelExport;
import com.zhanghe.poi.autoconfig.annotation.style.ColumnStyle;
import com.zhanghe.poi.autoconfig.annotation.type.ImageType;
import com.zhanghe.poi.autoconfig.entity.ExcelGroupSheets;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("excel-export")
public class ExportExcelController {

    @Autowired
    TestTypeServiceImpl testTypeService;

    /**
     * 可自动转换数据的导出展示
     *
     */
    @ExcelExport(exportClass = TestTypeExcelDTO.class)
    @RequestMapping("/testType")
    public List<TestTypeExcelDTO> testType(){
        List<TestTypeExcelDTO> testTypeExcelDTOS = testTypeService.exportList();
        return testTypeExcelDTOS;
    }

    /**
     * 可自动转换数据的导出展示
     *
     */
    @ExcelExport(exportClass = TestMergeDTO.class)
    @RequestMapping("/exportDTOS")
    public List<TestMergeDTO> exportDTOS(){
        //第一行
        TestMergeDTO testMergeDTO1 = new TestMergeDTO();
        testMergeDTO1.setOneLevel("格力");
        testMergeDTO1.setTwoLevel("冰箱");
        testMergeDTO1.setThreeLevel("空调");
        //第一列向下合并2行
        testMergeDTO1.setColumnStyle1(new ColumnStyle(2,0));
        //第二列向下合并1行
        testMergeDTO1.setColumnStyle2(new ColumnStyle(1,0));

        //第二行数据的第三列设置
        TestMergeDTO testMergeDTO2 = new TestMergeDTO();
        testMergeDTO2.setThreeLevel("洗衣机");

        //最后一行
        TestMergeDTO testMergeDTO3 = new TestMergeDTO();
        testMergeDTO3.setTwoLevel("热水器");
        testMergeDTO3.setThreeLevel("电风扇");
        //设置最后一行第三列的样式
        testMergeDTO3.setColumnStyle3(ColumnStyle.styleBuilder()
//                .cellStyle((cellStyle -> {}))   //创建新样式
                .fontProperty((f)->f.setColor(IndexedColors.RED.index)) //设置当中的字体颜色为红色
                .build());
        return Arrays.asList(testMergeDTO1,testMergeDTO2,testMergeDTO3);
    }

    @ExcelExport("manySheet")
    @RequestMapping("/exportManySheet")
    public ExcelGroupSheets exportManySheet() throws IOException {
        ExcelGroupSheets excelGroupSheets = new ExcelGroupSheets();
        //第一页sheet数据
        excelGroupSheets.putSheetValues(0, Collections.singletonList(new Sheet1("1","2","3")));


        //第二页数据
        Sheet2 sheet2 = new Sheet2();
        sheet2.setGeTongZhi(new BigDecimal(9789798));
        sheet2.setPerformance(445D);
        sheet2.setQuotient("份额:78789");
        //创建图片缓冲区
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics2D g = (Graphics2D) image.getGraphics();
        //绘制
        g.setColor(new Color(66, 108, 255));
//        g.fillRect(0, 0, 50, 50);
        g.drawString("测试树",10,10);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ImageIO.write(image, "JPEG",bytes);
        sheet2.setImageType(new ImageType(bytes.toByteArray(),ImageType.PICTURE_TYPE_JPEG));
        excelGroupSheets.putSheetValue(1,sheet2);

        //第三页
        excelGroupSheets.putSheetValues(2,Arrays.asList("准备的导入数据1","准备的导入数据2"));

        return excelGroupSheets;
    }
}
