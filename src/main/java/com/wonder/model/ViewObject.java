package com.wonder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: blank
 * @Date: 2020/1/2
 */
public class ViewObject {
    private Map<String,Object> map;
    public ViewObject(){
        map = new HashMap<>();
    }
    public void setViewObject(String key,Object obj){
        map.put(key,obj);
    }
    public Object getViewObject(String key){
        return map.get(key);
    }
}
