package com.zhanghe.poi.autoconfig.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface ExcelParam {
    /*
     * 配置名
     */
    @AliasFor("configName")
    String value() default "";

    @AliasFor("value")
    String configName() default "";



    /*
     *  上传的文件的表单参数名称key
     */
    String name() default "";
}
