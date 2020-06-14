package com.zhanghe.exception;

/**
 * 一个来自excel的异常 全局捕获它，来响应处理异常信息
 */
public class ExcelException extends RuntimeException {
    public ExcelException(String s) {
        super(s);
    }
}
