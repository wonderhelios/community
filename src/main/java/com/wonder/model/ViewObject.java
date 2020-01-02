package com.wonder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wonder
 * @Date: 2020/1/2
 */
public class ViewObject {
    private Map<String,Object> map = new HashMap<>();

    public void set(String key,Object obj){
        map.put(key,obj);
    }
    public Object get(String key){
        return map.get(key);
    }
}
