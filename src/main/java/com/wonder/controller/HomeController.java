package com.wonder.controller;

import com.wonder.model.*;
import com.wonder.service.CommentService;
import com.wonder.service.FollowService;
import com.wonder.service.QuestionService;
import com.wonder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/1
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;

    private List<ViewObject> getQuestions(int userId,int offset,int limit){
        List<Question> questions = questionService.getLatestQuestion(userId,offset,limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question:questions) {
            ViewObject vo = new ViewObject();
            vo.set("question",question);
            vo.set("followCount",followService.getFollowerCount(EntityType.ENTITY_QUESTION,
                    question.getId()));
            vo.set("user",userService.selectUserById(question.getUserId()));

            vos.add(vo);
        }
        return vos;
    }
    @RequestMapping(path = {"/index","/"},method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("vos",getQuestions(0,0,10));
        return "index";
    }
    @RequestMapping(path = {"/user/{userId}"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model,
                            @PathVariable("userId")int userId){
        model.addAttribute("vos",getQuestions(userId,0,10));
        User user = userService.selectUserById(userId);
        ViewObject vo = new ViewObject();
        vo.set("user",user);
        vo.set("commentCount",commentService.getUserCommentCount(userId));
        vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,userId));
        vo.set("followingCount",followService.getFollowingCount(userId,EntityType.ENTITY_USER));
        if(hostHolder.getUser() != null){
            vo.set("followed",followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_USER,userId));
        }else{
            vo.set("followed",false);
        }
        model.addAttribute("profileUser",vo);
        return "profile";
    }

}
