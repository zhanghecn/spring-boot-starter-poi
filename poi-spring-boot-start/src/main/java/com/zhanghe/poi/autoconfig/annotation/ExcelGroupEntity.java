package com.zhanghe.poi.autoconfig.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 *   excel实体分组配置
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface ExcelGroupEntity {
    /*
     * 下面2个是对应配置组
     */
    @AliasFor("groupId")
    String value() default "";
    @AliasFor("value")
    String groupId() default "";

    /*
     * 模板名称
     */
    String templateName() default "";

    /*
     * 导入映射路径
     */
    String uri() default "";

    /*
     * 取消导出自动样式
     */
    boolean  noAutoStyle() default false;

    //开始行号
    int startRow() default  0;

    //sheet下标
    int sheetIndex() default 0;

    /*
     * 此配置有以下三个作用
     * 1.取自动样式的行(noAutoStyle=true的时候失效)
     * TODO:2.需要使用扩展头部信息的位置 3.autoCheckHeader 自动检查头部的位置
    */
    int headerRow() default 0;


    /*
     * 此配置有以下2个作用
     * 1.取自动样式的列(noAutoStyle=true的时候失效) TODO:2.autoCheckHeader 自动检查头部的位置
     */
    int headerStartCol() default 0;

    /*
     * 导入自动检验头部
     */
    boolean autoCheckHeader() default  false;


    /*
     * 用作没有模板读取或者导出的时候指定最多列数量
     */
    int maxColumnNum() default 0;

    /*
     * 是否为一个标准模板 默认true 不标准
     */
    boolean notStandard() default true;
}
