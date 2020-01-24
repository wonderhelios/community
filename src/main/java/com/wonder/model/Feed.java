package com.wonder.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

/**
 * @Author: wonder
 * @Date: 2020/1/21
 */
@SuppressWarnings({"ALL", "AlibabaLowerCamelCaseVariableNaming"})
@Data
public class Feed {
    private int id;
    private int type;
    private int userId;
    private Date createdDate;
    private String data;
    private JSONObject dataJSON = null;

    public String get(String key){
        return dataJSON == null ? null : dataJSON.getString(key);
    }
}
