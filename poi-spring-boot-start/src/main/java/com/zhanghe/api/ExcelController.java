package com.zhanghe.api;


import com.zhanghe.autoconfig.entity.ExcelEntity;
import com.zhanghe.autoconfig.web.URIPathMappingHandlerAdapter;
import com.zhanghe.exception.RRException;
import com.zhanghe.util.ExcelMapperUtil;
import com.zhanghe.util.excel.mapper.ExcelMappersEntity;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

@RequestMapping("/excel")
/**
 * excel集成导入导出API
 */
@RestController
public class ExcelController implements ApplicationContextAware {
    public static final  String excelFileName = "excelFile";
    ExcelMapperUtil excelMapperUtil;
    @Autowired
    //获取请求映射处理
            RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    URIPathMappingHandlerAdapter uriPathMappingHandlerAdapter;

    @PostConstruct
    public void init(){
        this.uriPathMappingHandlerAdapter = new URIPathMappingHandlerAdapter(requestMappingHandlerAdapter);
        this.uriPathMappingHandlerAdapter.setApplicationContext(applicationContext);
    }
    @PostMapping("/import/{configName}")
    public void importExcel( @PathVariable("configName") String configName,
                             @RequestParam(excelFileName) List<MultipartFile> multipartFiles,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        if(multipartFiles.isEmpty()){
            throw new RRException(String.format("表单请求中未检查%s的文件",excelFileName));
        }
        ExcelMapperUtil excelMapperUtil=      getExcelMapperUtil();

        List<ExcelEntity> loadGroups = excelMapperUtil.loadGroups(configName);

        if(loadGroups.isEmpty()){
           Assert.isTrue(false,"没有此配置文件:"+configName);
       }

        String uri = ExcelMapperUtil.getURI(loadGroups);
        //获取映射路径的方法
        HttpServletRequest mapperRequest = (HttpServletRequest) Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(),
                new Class[]{HttpServletRequest.class},
                new MapperPathMethod(uri,request)
                );
        //获取执行链，可被拦截器拦截
        HandlerExecutionChain handler = requestMappingHandlerMapping.getHandler(mapperRequest);

        //通过MultipartFile获取包装excelEntity
        ExcelMappersEntity excelMappersEntity = ExcelMappersEntity.multipartFileMappersEntity(loadGroups,multipartFiles);

        //设置临时值
        uriPathMappingHandlerAdapter.setCurrentValues(new Object[]{excelMappersEntity});
        //执行方法
        try {
            uriPathMappingHandlerAdapter.handle(mapperRequest, response, handler.getHandler());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RRException(e.getMessage());
        }finally {
            excelMappersEntity.close();
        }
    }




    private ExcelMapperUtil getExcelMapperUtil() {
        if(excelMapperUtil==null){
             excelMapperUtil = ExcelMapperUtil.getExcelMapperUtil(this.applicationContext);
        }
        return excelMapperUtil;
    }

    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Data
    public static class MapperPathMethod implements InvocationHandler{
         private String mapperPath;
        private HttpServletRequest request;

        public MapperPathMethod(String mapperPath, HttpServletRequest request) {
            this.mapperPath = mapperPath;
            this.request = request;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //getServletPath
            String name = method.getName();
            if(name.equals("getRequestURI")||name.equals("getServletPath")){
                return mapperPath;
            }
            return method.invoke(request,args);
        }
    }
}
