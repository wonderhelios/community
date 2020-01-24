package com.wonder.service;

import com.wonder.model.Message;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/24
 */
public interface MessageService {

    /**
     * 添加消息
     * @param message
     * @return
     */
    int addMessage(Message message);

    /**
     * 获取会话详情
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> getConversationDetail(String conversationId,int offset,int limit);

    /**
     * 获取会话列表
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> getConversationList(int userId, int offset, int limit);

    /**
     * 获取会话数量
     * @param userId
     * @param conversationId
     * @return
     */
    int getConversationCount(int userId,String conversationId);
}
