package com.wonder.service;

import com.wonder.model.Feed;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/24
 */
public interface FeedService {
    /**
     * 添加新鲜事
     * @param feed
     * @return
     */
    boolean addFeed(Feed feed);

    /**
     * 获取用户新鲜事
     * @param maxId
     * @param userIds
     * @param count
     * @return
     */
    List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count);

    /**
     * 根据新鲜事id获取新鲜事
     * @param id
     * @return
     */
    Feed getById(int id);
}
