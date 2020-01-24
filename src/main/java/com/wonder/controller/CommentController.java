package com.wonder.controller;

import com.wonder.async.EventModel;
import com.wonder.async.EventProducer;
import com.wonder.async.EventType;
import com.wonder.constant.UserConst;
import com.wonder.model.Comment;
import com.wonder.constant.EntityTypeConst;
import com.wonder.model.HostHolder;
import com.wonder.service.impl.CommentServiceImpl;
import com.wonder.service.impl.QuestionServiceImpl;
import com.wonder.service.impl.SensitiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

/**
 * @Author: wonder
 * @Date: 2020/1/10
 */
@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentServiceImpl commentServiceImpl;
    @Autowired
    private SensitiveService sensitiveService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private QuestionServiceImpl questionServiceImpl;
    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/addComment"},method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId")int questionId,
                             @RequestParam("content")String content){
        try {
            //敏感词过滤
            content = HtmlUtils.htmlEscape(content);
            content = sensitiveService.filter(content);

            Comment comment = new Comment();
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityTypeConst.ENTITY_QUESTION);
            comment.setStatus(0);
            if(null == hostHolder.getUser()){
                comment.setUserId(UserConst.ANNOYMOUS_USERID);
            }else {
                comment.setUserId(hostHolder.getUser().getId());
            }
            commentServiceImpl.addComment(comment);
            //更新题目区里的评论数
            int count = commentServiceImpl.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionServiceImpl.updateCommentCount(comment.getEntityType(),count);

            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(comment.getUserId())
                    .setEntityId(questionId));

        }catch (Exception e){
            logger.error("添加评论失败:",e);
        }
        return "redirect:/question/" + String.valueOf(questionId);
    }
}
