package com.wonder.util;

/**
 * @Author: wonder
 * @Date: 2020/1/13
 */
public class RedisKeyUtil {
    private final static String SPLIT = ":";
    private final static String BIZ_LIKE = "LIKE";
    private final static String BIZ_DISLIKE = "DISLIKE";

    public static String getLikeKey(int entityType,int entityId){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    public static String getDisLikeKey(int entityType,int entityId){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
}
