package com.wonder.controller;

import com.wonder.constant.EntityTypeConst;
import com.wonder.model.*;
import com.wonder.service.impl.*;
import com.wonder.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/7
 */
@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    private QuestionServiceImpl questionServiceImpl;
    @Autowired
    private CommentServiceImpl commentServiceImpl;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private LikeServiceImpl likeServiceImpl;
    @Autowired
    private FollowServiceImpl followServiceImpl;

    @RequestMapping(value = "/question/add",method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title")String title,
                              @RequestParam("content")String content){
        try{
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if(hostHolder.getUser() == null){
                return JsonUtil.getJsonString(999);
            }else{
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionServiceImpl.addQuestion(question) > 0){
                return JsonUtil.getJsonString(0);
            }
            questionServiceImpl.addQuestion(question);

        }catch (Exception e){
            logger.error("增加问题失败:",e);
        }
        return JsonUtil.getJsonString(1,"失败");
    }
    @RequestMapping(value = "/question/{qid}",method = RequestMethod.GET)
    public String questionDetail(Model model,
                                 @PathVariable("qid")int qid){
        Question question = questionServiceImpl.getQuestionById(qid);
        model.addAttribute("question",question);
        model.addAttribute("user", userServiceImpl.selectUserById(question.getUserId()));

        List<Comment> commentList = commentServiceImpl.getCommentByEntity(qid, EntityTypeConst.ENTITY_QUESTION);
        List<ViewObject> vos = new ArrayList<>();
        for (Comment comment:commentList){
            ViewObject vo = new ViewObject();
            vo.set("comment",comment);
            vo.set("user", userServiceImpl.selectUserById(comment.getUserId()));

            if(hostHolder.getUser() == null){
                vo.set("liked",0);
            }else{
                vo.set("liked", likeServiceImpl.getLikeStatus(hostHolder.getUser().getId(),
                        EntityTypeConst.ENTITY_COMMENT,comment.getId()));
            }
            vo.set("likeCount", likeServiceImpl.getLikeCount(EntityTypeConst.ENTITY_COMMENT,comment.getId()));
            vos.add(vo);
        }
        model.addAttribute("comments",vos);
        List<ViewObject> followUsers = new ArrayList<>();
        List<Integer> users = followServiceImpl.getFollowers(EntityTypeConst.ENTITY_QUESTION,qid,20);
        for(Integer userId:users){
            ViewObject vo = new ViewObject();
            User u = userServiceImpl.selectUserById(userId);
            if(u == null){
                continue;
            }
            vo.set("name",u.getName());
            vo.set("headUrl",u.getHeadUrl());
            vo.set("id",u.getId());
            followUsers.add(vo);
        }
        if(hostHolder.getUser() != null){
            model.addAttribute("followed",
                    followServiceImpl.isFollower(hostHolder.getUser().getId(), EntityTypeConst.ENTITY_QUESTION,qid));
        }else{
            model.addAttribute("followed",false);
        }
        return "detail";
    }
}
