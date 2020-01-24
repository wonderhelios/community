package com.wonder.async;

import com.alibaba.fastjson.JSONObject;
import com.wonder.util.JedisAdapter;
import com.wonder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: wonder
 * @Date: 2020/1/14
 */
@Service
public class EventProducer {

    @Autowired
    private JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try{
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
