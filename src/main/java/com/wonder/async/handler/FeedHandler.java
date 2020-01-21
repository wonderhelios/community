package com.wonder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.wonder.async.EventHandler;
import com.wonder.async.EventModel;
import com.wonder.async.EventType;
import com.wonder.model.EntityType;
import com.wonder.model.Feed;
import com.wonder.model.Question;
import com.wonder.model.User;
import com.wonder.service.FeedService;
import com.wonder.service.FollowService;
import com.wonder.service.QuestionService;
import com.wonder.service.UserService;
import com.wonder.util.JedisAdapter;
import com.wonder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Author: wonder
 * @Date: 2020/1/21
 */
@Component
public class FeedHandler implements EventHandler {

    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FeedService feedService;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapter jedisAdapter;

    private String buildData(EventModel model) {
        Map<String, String> map = new HashMap<>();
        User actor = userService.selectUserById(model.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        if (model.getType() == EventType.COMMENT ||
                (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
            Question question = questionService.getQuestionById(model.getEntityId());
            if(question == null){
                return null;
            }
            map.put("questionId",String.valueOf(question.getId()));
            map.put("questionTitle",question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel model) {
        //构造新鲜事
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(model.getType().getValue());
        feed.setData(buildData(model));

        if(feed.getData() == null){
            return;
        }
        feedService.addFeed(feed);
        //获取所有粉丝
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER,model.getActorId(),Integer.MAX_VALUE);
        followers.add(0);
        for(int follower : followers){
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey,String.valueOf(feed.getId()));
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT,EventType.FOLLOW});
    }
}
