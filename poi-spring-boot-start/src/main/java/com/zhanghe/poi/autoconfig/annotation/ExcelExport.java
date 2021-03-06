package com.zhanghe.poi.autoconfig.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * excel对象导出下载
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface ExcelExport {
    @AliasFor("configBeanName")
    String value() default "";
    @AliasFor("value")
    String configBeanName() default "";

    String exportFileName() default "file";

    Class exportClass() default Class.class;

    /**
     * 打包zip
     *  是否可以打包
     * @return 返回打包是否可以打包
     */
    boolean packageZIP() default false;
}
