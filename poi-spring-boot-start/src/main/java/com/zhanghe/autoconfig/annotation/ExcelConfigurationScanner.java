package com.zhanghe.autoconfig.annotation;


import com.zhanghe.autoconfig.config.AnnotationMapperConfiguration;
import com.zhanghe.autoconfig.config.ExcelGroupEntityAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({AnnotationMapperConfiguration.class, ExcelGroupEntityAutoConfiguration.GroupEntityRegisterConfiguration.class})
public @interface ExcelConfigurationScanner {
    //扫描的配置包
    @AliasFor("packages")
    String[] value() default "";

    @AliasFor("value")
    String[] packages() default "";

    //excel模板位置
    String templateLocation() default "";
}
