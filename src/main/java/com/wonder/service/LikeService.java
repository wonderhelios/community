package com.wonder.service;

/**
 * @Author: wonder
 * @Date: 2020/1/24
 */
public interface LikeService {

    /**
     * 获取赞的数量
     * @param entityType
     * @param entityId
     * @return
     */
    long getLikeCount(int entityType,int entityId);

    /**
     * 获取赞的状态
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    long getLikeStatus(int userId,int entityType,int entityId);

    /**
     * 点赞
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    long like(int userId,int entityType,int entityId);

    /**
     * 点踩
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    long disLike(int userId,int entityType,int entityId);
}

