package com.zhanghe.autoconfig.annotation;

import com.zhanghe.autoconfig.annotation.type.OutType;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface ExcelColumn {

    @AliasFor("columnIndex")
    int value() default 0;
    @AliasFor("value")
    int columnIndex() default 0;


    //列名称,使用此列,columnIndex将舍弃
    String columnName() default "";
    //1.导入导出时候，日期转换字符串使用
    String dateFormat() default "";
    //导出数据的类型
    OutType outType() default OutType.CUSTOM;
}
