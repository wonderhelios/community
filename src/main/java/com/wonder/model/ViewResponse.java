package com.wonder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wonder
 * @Date: 2020/1/24
 */
public class ViewResponse<T> {
    private Map<String,T> map = new HashMap<>();

    public void set(String key,T obj){
        map.put(key,obj);
    }
    public T get(String key){
        return map.get(key);
    }
}
