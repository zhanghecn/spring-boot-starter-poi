package com.zhanghe.autoconfig.entity.extend;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 抽离出来的配置
 * @Author: ZhangHe
 * @Date: 2020/5/23 14:00
 */
@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractExcelConfiguration {
    //模板文件名称
    protected String templateName;

    //模板位置
    protected String templateLocation;

    //映射的类信息
    protected Class clazz;

    //控制路径
    protected String uri;

    /**
     * 是否取消自动样式用来提高导出效率
     * 默认是添加自动样式的
     * TODO:自动样式包含 (自适应文本长度)（自动根据列头样式）(自动换行)
     */
    protected boolean noAutoStyle;
}
