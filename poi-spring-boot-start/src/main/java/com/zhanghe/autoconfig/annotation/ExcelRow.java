package com.zhanghe.autoconfig.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/*
行的位置
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface ExcelRow {
    /**
     * 下面2个是对应的行对应的位置
     * @return
     */
    @AliasFor("rowIndex")
    int value() default 0;
    @AliasFor("value")
    int rowIndex() default 0;
}
