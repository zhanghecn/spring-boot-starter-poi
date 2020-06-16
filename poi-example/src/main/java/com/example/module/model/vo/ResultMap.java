package com.example.module.model.vo;

import java.util.HashMap;

public class ResultMap extends HashMap {

    public ResultMap(){
        put("code",0);
        put("msg","success");
    }

    public static ResultMap ok(){
        return new ResultMap();
    }

    public ResultMap add(Object key,Object value){
        this.put(key,value);
        return this;
    }

    public static ResultMap ok(Object data){
        return ok().add("data",data);
    }

}
