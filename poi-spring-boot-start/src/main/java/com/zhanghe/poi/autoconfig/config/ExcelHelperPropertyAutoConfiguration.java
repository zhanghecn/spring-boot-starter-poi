package com.zhanghe.poi.autoconfig.config;

import com.zhanghe.poi.util.SpringContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ZhangHe on 2020/6/12 23:41
 */
@Configuration
@Slf4j
public class ExcelHelperPropertyAutoConfiguration implements InitializingBean {

    @Bean
    @ConditionalOnMissingBean
    public SpringContextHelper springContextHelper(){
        return new SpringContextHelper();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("自动装载ExcelHelper");
    }
}
