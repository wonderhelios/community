package com.wonder.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @Author: wonder
 * @Date: 2020/1/7
 */
public class JsonUtil {

    public static String getJsonString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

    public static String getJsonString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);

        return json.toJSONString();
    }

    public static String getJsonString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }
}
