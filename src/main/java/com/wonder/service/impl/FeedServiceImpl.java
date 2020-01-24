package com.wonder.service.impl;

import com.wonder.dao.FeedDAO;
import com.wonder.model.Feed;
import com.wonder.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/21
 */
@Service
public class FeedServiceImpl implements FeedService {
    @Autowired
    private FeedDAO feedDAO;

    @Override
    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count){
        return feedDAO.selectUserFeeds(maxId,userIds,count);
    }
    @Override
    public boolean addFeed(Feed feed){
        feedDAO.addFeed(feed);
        return feed.getId() > 0;
    }
    @Override
    public Feed getById(int id){
        return feedDAO.getFeedById(id);
    }
}
