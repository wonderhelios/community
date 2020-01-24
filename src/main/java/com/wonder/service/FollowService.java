package com.wonder.service;

import com.wonder.util.JedisAdapter;
import com.wonder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: wonder
 * @Date: 2020/1/24
 */
public interface FollowService {
    /**
     * 关注新对象
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    boolean follow(int userId,int entityType,int entityId);

    /**
     * 取关新对象
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    boolean unFollow(int userId,int entityType,int entityId);

    /**
     * 获取关注对象
     * @param entityType
     * @param entityId
     * @param count
     * @return
     */
    List<Integer> getFollowers(int entityType,int entityId,int count);

    /**
     * 获取粉丝对象
     * @param userId
     * @param entityType
     * @param count
     * @return
     */
    List<Integer> getFollowings(int userId,int entityType,int count);

    /**
     * 获取关注对象
     * @param userId
     * @param entityType
     * @param offset
     * @param count
     * @return
     */
    List<Integer> getFollowers(int userId, int entityType, int offset, int count);

    /**
     * 获取粉丝对象
     * @param userId
     * @param entityType
     * @param offset
     * @param count
     * @return
     */
    List<Integer> getFollowings(int userId, int entityType, int offset, int count);

    /**
     * 从redis获取id
     * @param jdiset
     * @return
     */
    List<Integer> getIdsFromSet(Set<String> jdiset);

    /**
     * 获取关注对象的数量
     * @param entityType
     * @param entityId
     * @return
     */
    long getFollowerCount(int entityType,int entityId);

    /**
     * 获取粉丝对象数量
     * @param userId
     * @param entityType
     * @return
     */
    long getFollowingCount(int userId,int entityType);

    /**
     * 判断是否为关注对象
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    boolean isFollower(int userId,int entityType,int entityId);
}

