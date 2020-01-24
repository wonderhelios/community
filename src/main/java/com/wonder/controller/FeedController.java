package com.wonder.controller;

import com.wonder.constant.EntityTypeConst;
import com.wonder.model.Feed;
import com.wonder.model.HostHolder;
import com.wonder.service.impl.FeedServiceImpl;
import com.wonder.service.impl.FollowServiceImpl;
import com.wonder.util.JedisAdapter;
import com.wonder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/21
 */

@Controller
public class FeedController {

    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private JedisAdapter jedisAdapter;
    @Autowired
    private FeedServiceImpl feedServiceImpl;
    @Autowired
    private FollowServiceImpl followServiceImpl;


    @RequestMapping(path = "/pushfeeds", method = {RequestMethod.GET, RequestMethod.POST})
    public String getPushFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);
        List<Feed> feeds = new ArrayList<>();
        for (String feedId : feedIds) {
            Feed feed = feedServiceImpl.getById(Integer.parseInt(feedId));
            if(feed != null){
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds",feeds);
        return "feeds";
    }
    @RequestMapping(path = "/pullfeeds",method = {RequestMethod.GET,RequestMethod.POST})
    public String getPullFeeds(Model model){
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<Integer> followings = new ArrayList<>();
        if(localUserId != 0){
            followings = followServiceImpl.getFollowings(localUserId, EntityTypeConst.ENTITY_USER,Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedServiceImpl.getUserFeeds(Integer.MAX_VALUE,followings,10);
        model.addAttribute("feeds",feeds);
        return "feeds";
    }
}
