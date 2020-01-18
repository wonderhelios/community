package com.wonder.controller;

import com.wonder.async.EventModel;
import com.wonder.async.EventProducer;
import com.wonder.async.EventType;
import com.wonder.model.*;
import com.wonder.service.CommentService;
import com.wonder.service.FollowService;
import com.wonder.service.QuestionService;
import com.wonder.service.UserService;
import com.wonder.util.WonderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wonder
 * @Date: 2020/1/18
 */
@Controller
public class FollowController {

    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = "/followUser", method = RequestMethod.POST)
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return WonderUtils.getJSONString(999);
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(),
                EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityType(EntityType.ENTITY_USER)
                .setEntityId(userId)
                .setEntityOwnerId(userId));
        //返回关注的对象数目
        return WonderUtils.getJSONString(ret ? 0 : 1,
                String.valueOf(followService.getFollowerCount(hostHolder.getUser().getId(),
                        EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = "/unfollowUser", method = RequestMethod.POST)
    @ResponseBody
    public String unFollowUser(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return WonderUtils.getJSONString(999);
        }
        boolean ret = followService.unFollow(hostHolder.getUser().getId(),
                EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityType(EntityType.ENTITY_USER)
                .setEntityId(userId)
                .setEntityOwnerId(userId));
        //返回关注的对象数目
        return WonderUtils.getJSONString(ret ? 0 : 1,
                String.valueOf(followService.getFollowerCount(hostHolder.getUser().getId(),
                        EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = "/followQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return WonderUtils.getJSONString(999);
        }
        Question q = questionService.getQuestionById(questionId);
        if (q == null) {
            return WonderUtils.getJSONString(1, "问题不存在.");
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(),
                EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityId(questionId)
                .setEntityOwnerId(questionId));
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));

        return WonderUtils.getJSONString(ret ? 0 : 1, info);
    }

    @RequestMapping(path = "/unfollowQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String unFollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return WonderUtils.getJSONString(999);
        }
        Question q = questionService.getQuestionById(questionId);
        if (q == null) {
            return WonderUtils.getJSONString(1, "问题不存在.");
        }
        boolean ret = followService.unFollow(hostHolder.getUser().getId(),
                EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityId(questionId)
                .setEntityOwnerId(questionId));
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));

        return WonderUtils.getJSONString(ret ? 0 : 1, info);
    }
    @RequestMapping(path = "/user/{uid}/followers",method = RequestMethod.GET)
    public String followers(Model model,
                            @PathVariable("uid")int userId){
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER,userId,0,10);
        if(hostHolder.getUser() != null){
            model.addAttribute("followers",getUsersInfo(hostHolder.getUser().getId(),followerIds));
        }else{
            model.addAttribute("followers",getUsersInfo(0,followerIds));
        }
        model.addAttribute("followerCount",
                followService.getFollowerCount(EntityType.ENTITY_USER,userId));
        model.addAttribute("curUser",userService.selectUserById(userId));
        return "followers";
    }
    @RequestMapping(path = "/user/{uid}/followings",method = RequestMethod.GET)
    public String followings(Model model,
                            @PathVariable("uid")int userId){
        List<Integer> followingIds = followService.getFollowings(userId,EntityType.ENTITY_USER,0,10);
        if(hostHolder.getUser() != null){
            model.addAttribute("followings",getUsersInfo(hostHolder.getUser().getId(),followingIds));
        }else{
            model.addAttribute("followings",getUsersInfo(0,followingIds));
        }
        model.addAttribute("followingCount",
                followService.getFollowingCount(userId,EntityType.ENTITY_USER));
        model.addAttribute("curUser",userService.selectUserById(userId));
        return "followings";
    }
    private List<ViewObject> getUsersInfo(int localUserId,List<Integer> userIds){
        List<ViewObject> userInfos = new ArrayList<>();
        for(Integer uid:userIds){
            User user = userService.selectUserById(uid);
            if(user == null){
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user",user);
            vo.set("commentCount",commentService.getUserCommentCount(uid));
            vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,uid));
            if(localUserId != 0){
                vo.set("followed",followService.isFollower(localUserId,EntityType.ENTITY_USER,uid));
            }else{
                vo.set("followed",false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
