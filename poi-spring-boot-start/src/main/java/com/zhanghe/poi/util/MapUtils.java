/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.zhanghe.poi.util;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Map工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
public class MapUtils extends HashMap<String, Object> {

    @Override
    public MapUtils put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 逆转key value
     * @param map
     * @param <TWO>
     * @param <ONE>
     * @return
     */
    public static <TWO,ONE>Map<TWO, ONE> valueKeyReverse(Map<ONE,TWO> map){
        if(map==null){
            return null;
        }
        Set<Entry<ONE, TWO>> entries = map.entrySet();
        Map<TWO,ONE> reverse = new HashMap<TWO,ONE>();
        for (Entry<ONE, TWO> entry : entries) {
            reverse.put(entry.getValue(),entry.getKey());
        }
        return reverse;
    }
}
