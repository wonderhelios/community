package com.wonder.service;

import com.wonder.util.JedisAdapter;
import com.wonder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: wonder
 * @Date: 2020/1/17
 */
@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean follow(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followingKey = RedisKeyUtil.getFollowingKey(userId,entityType);
        Date date = new Date();
        /** 为实体粉丝增加当前用户 **/
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zadd(followerKey,date.getTime(),String.valueOf(userId));
        /** 当前用户的关注实体+1 **/
        tx.zadd(followingKey,date.getTime(),String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx,jedis);
        return ret.size() == 2  && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }
    public boolean unFollow(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followingKey = RedisKeyUtil.getFollowingKey(userId,entityType);
        /** 为实体粉丝移除当前用户 **/
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zrem(followerKey,String.valueOf(userId));
        /** 当前用户的关注实体-1 **/
        tx.zrem(followingKey,String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx,jedis);
        return ret.size() == 2  && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }
    public List<Integer> getFollowers(int entityType,int entityId,int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey,0,count));
    }
    public List<Integer> getFollowings(int userId,int entityType,int count){
        String followingKey = RedisKeyUtil.getFollowingKey(userId,entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followingKey,0,count));
    }
    public List<Integer> getFollowers(int userId, int entityType, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, offset+count));
    }
    public List<Integer> getFollowings(int userId, int entityType, int offset, int count) {
        String followingKey = RedisKeyUtil.getFollowingKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followingKey, offset, offset+count));
    }
    public List<Integer> getIdsFromSet(Set<String> jdset){
        List<Integer> ids = new ArrayList<>();
        for(String str:jdset){
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    public long getFollowerCount(int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapter.zcard(followerKey);
    }
    public long getFollowingCount(int userId,int entityType){
        String followingKey = RedisKeyUtil.getFollowingKey(userId,entityType);
        return jedisAdapter.zcard(followingKey);
    }

    public boolean isFollower(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapter.zscore(followerKey,String.valueOf(userId)) != null;
    }
}
