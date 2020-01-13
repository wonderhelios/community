package com.wonder.controller;

import com.wonder.model.*;
import com.wonder.service.CommentService;
import com.wonder.service.LikeService;
import com.wonder.service.QuestionService;
import com.wonder.service.UserService;
import com.wonder.util.WonderUtils;
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
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;

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
//              question.setUserId(WonserUtils.ANNOYMOUS_USERID);
                return WonderUtils.getJSONString(999);
            }else{
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question) > 0){
                return WonderUtils.getJSONString(0);
            }
            questionService.addQuestion(question);

        }catch (Exception e){
            logger.error("增加问题失败:" + e.getMessage());
        }
        return WonderUtils.getJSONString(1,"失败");
    }
    @RequestMapping(value = "/question/{qid}",method = RequestMethod.GET)
    public String questionDetail(Model model,
                                 @PathVariable("qid")int qit){
        Question question = questionService.getQuestionById(qit);
        model.addAttribute("question",question);
        model.addAttribute("user",userService.selectUserById(question.getUserId()));

        List<Comment> commentList = commentService.getCommentByEntity(qit, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos = new ArrayList<>();
        for (Comment comment:commentList){
            ViewObject vo = new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.selectUserById(comment.getUserId()));

            if(hostHolder.getUser() == null){
                vo.set("liked",0);
            }else{
                vo.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),
                        EntityType.ENTITY_COMMENT,comment.getId()));
            }
            vo.set("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
            vos.add(vo);
        }
        model.addAttribute("comments",vos);
        return "detail";
    }
}
