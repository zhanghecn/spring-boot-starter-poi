package com.zhanghe.autoconfig.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

//@Configuration
@Slf4j
@Deprecated
public class XmlMapperConfiguration {
    @Value("${excel.xml.config.location}")
    private String configPreLocation;
    @Value("${excel.template.location}")
    private String templatePreLocation;
    @Autowired
    RequestMappingHandlerAdapter requestMappingHandlerAdapter;
//    @PostConstruct
//    //过滤掉xml转换器,因为自动配置类会默认加载
//    public void filter() throws IllegalAccessException {
//        //获取所有的返回值处理
//        List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
//        for (HandlerMethodReturnValueHandler returnValueHandler : returnValueHandlers) {
//                //如果是responseBody的处理
//            if(returnValueHandler instanceof RequestResponseBodyMethodProcessor){
//                RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor=   (RequestResponseBodyMethodProcessor)returnValueHandler;
//                Field messageConverters = ReflectionUtils.findField(AbstractMessageConverterMethodProcessor.class, "messageConverters");
//                ReflectionUtils.makeAccessible(messageConverters);
//                //获取消息转换器
//                List<HttpMessageConverter<?>> messageConverterList= (List<HttpMessageConverter<?>>) messageConverters.get(requestResponseBodyMethodProcessor);
//            //要删除的下标
//                List<Integer> integers = new ArrayList<>(messageConverterList.size()/2);
//                for (int i = 0; i < messageConverterList.size(); i++) {
//                    HttpMessageConverter<?> httpMessageConverter = messageConverterList.get(i);
//                    //如果是Xml消息转换器
//                    if(httpMessageConverter instanceof MappingJackson2XmlHttpMessageConverter){
//                        integers.add(i);
//                    }
//                }
//                Collections.reverse(integers);
//                //删除
//                for (int index : integers) {
//                    messageConverterList.remove(index);
//                }
//                return;
//            }
//        }
//    }
//    @Bean
//    //xml映射类
//    public XmlMapper xmlMapper(){
//        XmlMapper xmlMapper = new XmlMapper();
//        xmlMapper.setDefaultUseWrapper(false);
//        //字段为null，自动忽略，不再序列化
//        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        //XML标签名:使用骆驼命名的属性名，
//        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
//        //设置转换模式
//        xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
//        return xmlMapper;
//    }

//    @Bean
//    //映射工具类
//    public ExcelMapperTemplate xmlMapperTemplate(XmlMapper xmlMapper){
//        log.info(String.format("excel配置路径为:%s,模板路径为:%s", configPreLocation,templatePreLocation));
//        return new ExcelMapperTemplate(configPreLocation,templatePreLocation,xmlMapper);
//    }

}
