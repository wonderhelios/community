package com.wonder.util;

/**
 * @Author: wonder
 * @Date: 2020/1/13
 */
public class RedisKeyUtil {
    private final static String SPLIT = ":";
    private final static String BIZ_LIKE = "LIKE";
    private final static String BIZ_DISLIKE = "DISLIKE";
    private final static String BIZ_EVENTQUEUE = "EVENTQUEUE";
    /** 粉丝 **/
    private final static String BIZ_FOLLOWER = "FOLLOWER";
    /** 关注对象 **/
    private final static String BIZ_FOLLOWING = "FOLLOWING";
    private final static String BIZ_TIMELINE = "TIMELINE";

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
