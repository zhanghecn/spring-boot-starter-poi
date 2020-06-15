package com.zhanghe.poi.util.excel.sheet.row.cell;

import org.springframework.util.StringUtils;

public interface ExcelColumnNameUtils {

    /**
     * 该方法用来将Excel中的ABCD列转换成具体的数据
     * @param column:ABCD列名称
     * @return integer：将字母列名称转换成数字
     * **/
     static int excelColStrToNum(String column) {
        int num = 0;
        int result = 0;
        int length =column.length();
        for(int i = 0; i < length; i++) {
            char ch = column.charAt(length - i - 1);
            num = (int)(ch - 'A' + 1) ;
            num *= Math.pow(26, i);
            result += num;
        }
        return result;
    }
    //头部标识是否为ABCD..字母名称
    static boolean isHeader(String header) {
         if(!StringUtils.hasText(header)){
             return false;
         }
        long count = header.chars().filter((c) -> c >= 'A' && c <= 'Z').count();
        if(count!=header.length()){
            return false;
        }
        return true;
    }
    /**
     * 该方法用来将具体的数据转换成Excel中的ABCD列
     * @param int：需要转换成字母的数字
     * @return column:ABCD列名称
     * **/
     static String excelColIndexToStr(int columnIndex) {
        if (columnIndex <= 0) {
            return null;
        }
        String columnStr = "";
        columnIndex--;
        do {
            if (columnStr.length() > 0) {
                columnIndex--;
            }
            columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
            columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
        } while (columnIndex > 0);
        return columnStr;
    }
}
