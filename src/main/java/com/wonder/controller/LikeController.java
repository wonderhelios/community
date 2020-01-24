package com.wonder.controller;

import com.wonder.async.EventModel;
import com.wonder.async.EventProducer;
import com.wonder.async.EventType;
import com.wonder.model.Comment;
import com.wonder.constant.EntityTypeConst;
import com.wonder.model.HostHolder;
import com.wonder.model.User;
import com.wonder.service.impl.CommentServiceImpl;
import com.wonder.service.impl.LikeServiceImpl;
import com.wonder.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: wonder
 * @Date: 2020/1/13
 */
@Controller
public class LikeController {

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private CommentServiceImpl commentServiceImpl;
    @Autowired
    private LikeServiceImpl likeServiceImpl;
    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId")int commentId){
        if(hostHolder.getUser() == null){
            return JsonUtil.getJsonString(999);
        }
        User user = hostHolder.getUser();
        Comment comment = commentServiceImpl.getCommentById(commentId);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                                .setActorId(hostHolder.getUser().getId())
                                .setEntityId(commentId)
                                .setEntityType(EntityTypeConst.ENTITY_COMMENT)
                                .setEntityOwnerId(comment.getUserId())
                                .setExt("questionId",String.valueOf(comment.getEntityId())));

        long likeCount = likeServiceImpl.like(user.getId(), EntityTypeConst.ENTITY_COMMENT,commentId);

        return JsonUtil.getJsonString(0,String.valueOf(likeCount));
    }
    @RequestMapping(path = "/dislike",method = RequestMethod.POST)
    @ResponseBody
    public String disLike(@RequestParam("commentId")int commentId){
        if(hostHolder.getUser() == null){
            return JsonUtil.getJsonString(999);
        }
        User user = hostHolder.getUser();
        long likeCount = likeServiceImpl.disLike(user.getId(), EntityTypeConst.ENTITY_COMMENT,commentId);
        return JsonUtil.getJsonString(0,String.valueOf(likeCount));
    }
}
