package com.wonder.controller;

import com.wonder.async.EventModel;
import com.wonder.async.EventProducer;
import com.wonder.async.EventType;
import com.wonder.model.Comment;
import com.wonder.model.EntityType;
import com.wonder.model.HostHolder;
import com.wonder.model.User;
import com.wonder.service.CommentService;
import com.wonder.service.LikeService;
import com.wonder.util.JedisAdapter;
import com.wonder.util.WonderUtils;
import org.apache.ibatis.annotations.Param;
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
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId")int commentId){
        if(hostHolder.getUser() == null){
            return WonderUtils.getJSONString(999);
        }
        User user = hostHolder.getUser();
        Comment comment = commentService.getCommentById(commentId);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                                .setActorId(hostHolder.getUser().getId())
                                .setEntityId(commentId)
                                .setEntityType(EntityType.ENTITY_COMMENT)
                                .setEntityOwnerId(comment.getUserId())
                                .setExt("questionId",String.valueOf(comment.getEntityId())));

        long likeCount = likeService.like(user.getId(), EntityType.ENTITY_COMMENT,commentId);

        return WonderUtils.getJSONString(0,String.valueOf(likeCount));
    }
    @RequestMapping(path = "/dislike",method = RequestMethod.POST)
    @ResponseBody
    public String disLike(@RequestParam("commentId")int commentId){
        if(hostHolder.getUser() == null){
            return WonderUtils.getJSONString(999);
        }
        User user = hostHolder.getUser();
        long likeCount = likeService.disLike(user.getId(),EntityType.ENTITY_COMMENT,commentId);
        return WonderUtils.getJSONString(0,String.valueOf(likeCount));
    }
}
