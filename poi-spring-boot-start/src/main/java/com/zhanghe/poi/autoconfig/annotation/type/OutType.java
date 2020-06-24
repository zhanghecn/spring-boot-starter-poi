package com.zhanghe.poi.autoconfig.annotation.type;

public enum OutType {
    CUSTOM, //按照属性的值来
    INDEX,   //按照自动生成的下标来，从1开始 TODO: 一定要是整数
    IMAGE   //导出的是图片
}
