package com.wonder.controller;

import com.wonder.async.EventModel;
import com.wonder.async.EventProducer;
import com.wonder.async.EventType;
import com.wonder.constant.EntityTypeConst;
import com.wonder.model.*;
import com.wonder.service.impl.CommentServiceImpl;
import com.wonder.service.impl.FollowServiceImpl;
import com.wonder.service.impl.QuestionServiceImpl;
import com.wonder.service.impl.UserServiceImpl;
import com.wonder.util.JsonUtil;
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
    private HostHolder hostHolder;
    @Autowired
    private FollowServiceImpl followServiceImpl;
    @Autowired
    private QuestionServiceImpl questionServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private CommentServiceImpl commentServiceImpl;
    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/followUser", method = RequestMethod.POST)
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return JsonUtil.getJsonString(999);
        }
        boolean ret = followServiceImpl.follow(hostHolder.getUser().getId(),
                EntityTypeConst.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityType(EntityTypeConst.ENTITY_USER)
                .setEntityId(userId)
                .setEntityOwnerId(userId));
        //返回关注的对象数目
        return JsonUtil.getJsonString(ret ? 0 : 1,
                String.valueOf(followServiceImpl.getFollowerCount(hostHolder.getUser().getId(),
                        EntityTypeConst.ENTITY_USER)));
    }

    @RequestMapping(path = "/unfollowUser", method = RequestMethod.POST)
    @ResponseBody
    public String unFollowUser(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return JsonUtil.getJsonString(999);
        }
        boolean ret = followServiceImpl.unFollow(hostHolder.getUser().getId(),
                EntityTypeConst.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityType(EntityTypeConst.ENTITY_USER)
                .setEntityId(userId)
                .setEntityOwnerId(userId));
        //返回关注的对象数目
        return JsonUtil.getJsonString(ret ? 0 : 1,
                String.valueOf(followServiceImpl.getFollowerCount(hostHolder.getUser().getId(),
                        EntityTypeConst.ENTITY_USER)));
    }

    @RequestMapping(path = "/followQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return JsonUtil.getJsonString(999);
        }
        Question q = questionServiceImpl.getQuestionById(questionId);
        if (q == null) {
            return JsonUtil.getJsonString(1, "问题不存在.");
        }
        boolean ret = followServiceImpl.follow(hostHolder.getUser().getId(),
                EntityTypeConst.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityType(EntityTypeConst.ENTITY_QUESTION)
                .setEntityId(questionId)
                .setEntityOwnerId(questionId));
        Map<String, Object> info = new HashMap<>(10);
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followServiceImpl.getFollowerCount(EntityTypeConst.ENTITY_QUESTION, questionId));

        return JsonUtil.getJsonString(ret ? 0 : 1, info);
    }

    @RequestMapping(path = "/unfollowQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String unFollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return JsonUtil.getJsonString(999);
        }
        Question q = questionServiceImpl.getQuestionById(questionId);
        if (q == null) {
            return JsonUtil.getJsonString(1, "问题不存在.");
        }
        boolean ret = followServiceImpl.unFollow(hostHolder.getUser().getId(),
                EntityTypeConst.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityType(EntityTypeConst.ENTITY_QUESTION)
                .setEntityId(questionId)
                .setEntityOwnerId(questionId));
        Map<String, Object> info = new HashMap<>(10);
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followServiceImpl.getFollowerCount(EntityTypeConst.ENTITY_QUESTION, questionId));

        return JsonUtil.getJsonString(ret ? 0 : 1, info);
    }
    @RequestMapping(path = "/user/{uid}/followers",method = RequestMethod.GET)
    public String followers(Model model,
                            @PathVariable("uid")int userId){
        List<Integer> followerIds = followServiceImpl.getFollowers(EntityTypeConst.ENTITY_USER,userId,0,10);
        if(hostHolder.getUser() != null){
            model.addAttribute("followers",getUsersInfo(hostHolder.getUser().getId(),followerIds));
        }else{
            model.addAttribute("followers",getUsersInfo(0,followerIds));
        }
        model.addAttribute("followerCount",
                followServiceImpl.getFollowerCount(EntityTypeConst.ENTITY_USER,userId));
        model.addAttribute("curUser", userServiceImpl.selectUserById(userId));
        return "followers";
    }
    @RequestMapping(path = "/user/{uid}/followings",method = RequestMethod.GET)
    public String followings(Model model,
                            @PathVariable("uid")int userId){
        List<Integer> followingIds = followServiceImpl.getFollowings(userId, EntityTypeConst.ENTITY_USER,0,10);
        if(hostHolder.getUser() != null){
            model.addAttribute("followings",getUsersInfo(hostHolder.getUser().getId(),followingIds));
        }else{
            model.addAttribute("followings",getUsersInfo(0,followingIds));
        }
        model.addAttribute("followingCount",
                followServiceImpl.getFollowingCount(userId, EntityTypeConst.ENTITY_USER));
        model.addAttribute("curUser", userServiceImpl.selectUserById(userId));
        return "followings";
    }
    private List<ViewObject> getUsersInfo(int localUserId,List<Integer> userIds){
        List<ViewObject> userInfos = new ArrayList<>();
        for(Integer uid:userIds){
            User user = userServiceImpl.selectUserById(uid);
            if(user == null){
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user",user);
            vo.set("commentCount", commentServiceImpl.getUserCommentCount(uid));
            vo.set("followerCount", followServiceImpl.getFollowerCount(EntityTypeConst.ENTITY_USER,uid));
            if(localUserId != 0){
                vo.set("followed", followServiceImpl.isFollower(localUserId, EntityTypeConst.ENTITY_USER,uid));
            }else{
                vo.set("followed",false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
