package com.wonder.service.impl;

import com.wonder.dao.MessageDAO;
import com.wonder.model.Message;
import com.wonder.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/11
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private SensitiveService sensitiveService;

    @Override
    public int addMessage(Message message){
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message);
    }
    @Override
    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }
    @Override
    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }
    @Override
    public int getConversationCount(int userId,String conversationId){
        return messageDAO.getConversationCount(userId,conversationId);
    }
}
