package com.wonder.util;

import com.wonder.constant.RedisKeyConst;

import static com.wonder.constant.RedisKeyConst.*;

/**
 * @Author: wonder
 * @Date: 2020/1/13
 */
public class RedisKeyUtil {

    public static String getLikeKey(int entityType,int entityId){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    public static String getDisLikeKey(int entityType,int entityId){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }
    public static String getFollowerKey(int entityType,int entityId){
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    public static String getFollowingKey(int userId,int entityType){
        return BIZ_FOLLOWING + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }
    public static String getTimelineKey(int userId){
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }
}
