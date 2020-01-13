package com.wonder.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author: wonder
 * @Date: 2020/1/7
 */
public class WonderUtils {

    public static final int ANNOYMOUS_USERID = 6;

    public static String getJSONString(int code){
        JSONObject json = new JSONObject();
        json.put("code",code);
        return json.toJSONString();
    }
    public static String getJSONString(int code,String msg){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);

        return json.toJSONString();
    }
}
