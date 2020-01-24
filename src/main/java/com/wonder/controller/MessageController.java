package com.wonder.controller;

import com.wonder.model.HostHolder;
import com.wonder.model.Message;
import com.wonder.model.User;
import com.wonder.model.ViewObject;
import com.wonder.service.impl.MessageServiceImpl;
import com.wonder.service.impl.UserServiceImpl;
import com.wonder.util.JsonUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/11
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageServiceImpl messageServiceImpl;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @RequestMapping(value = "/msg/list",method = RequestMethod.GET)
    public String conversationDetail(Model model){
        try{
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageServiceImpl.getConversationList(localUserId,0,10);
            for(Message conversation:conversationList){
                ViewObject vo = new ViewObject();
                vo.set("conversation",conversation);
                int targetId = conversation.getFromId() == localUserId ? conversation.getToId() : conversation.getFromId();
                User user = userServiceImpl.selectUserById(targetId);
                vo.set("user",user);
                vo.set("unread", messageServiceImpl.getConversationCount(localUserId,conversation.getConversationId()));

                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
        }catch (Exception e){
            logger.error("获取消息列表失败:",e);
        }
        return "letter";
    }

    @RequestMapping(value = "/msg/addMessage",method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("toName")String toName,
                             @RequestParam("content")String content){
        try {
            if(hostHolder.getUser() == null){
                return JsonUtil.getJsonString(999,"未登录");
            }
            if(userServiceImpl.selectUserByName(toName) == null){
                return JsonUtil.getJsonString(1,"用户不存在");
            }
            Message message = new Message();
            int fromId = hostHolder.getUser().getId();
            int toId = userServiceImpl.selectUserByName(toName).getId();

            message.setFromId(fromId);
            message.setToId(toId);
            message.setCreatedDate(new Date());
            message.setContent(content);
            message.setConversationId(fromId < toId ? String.format("%d_%d",fromId,toId):String.format("%d_%d",toId,fromId));

            messageServiceImpl.addMessage(message);
            return JsonUtil.getJsonString(0);
        }catch (Exception e){
            logger.error("添加站内信失败:",e);
            return JsonUtil.getJsonString(1,"添加站内信失败");
        }
    }
    @RequestMapping(value = "/msg/detail",method = RequestMethod.GET)
    public String conversationDetail(Model model, @Param("conversationId")String conversationId){
        try{
            List<Message> conversationList = messageServiceImpl.getConversationDetail(conversationId,0,10);
            List<ViewObject> vos = new ArrayList<>();
            for (Message conversation : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message",conversation);
                User user = userServiceImpl.selectUserById(conversation.getFromId());
                if(user == null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                vos.add(vo);
            }
            model.addAttribute("messages",vos);
        }catch (Exception e){
            logger.error("获取消息详情失败:",e);
        }
        return "letterDetail";
    }
}
