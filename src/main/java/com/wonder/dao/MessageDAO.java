package com.wonder.dao;

import com.wonder.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/11
 */

@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, created_date, conversation_id ";
    String SELECT_FIELDS = " id " + INSERT_FIELDS;

    /**
     * 添加Message
     * @param message
     * @return
     */
    @Insert({"insert into ",TABLE_NAME," ( ",INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{createdDate},#{conversationId})"})
    int addMessage(Message message);

    /**
     * 获取会话详情
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,
            " where conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId")String conversationId,
                                        @Param("offset")int offset,
                                        @Param("limit")int limit);

    /**
     * 获取会话列表
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME,
            " where from_id=#{userId} or to_id=#{userId} order by created_date desc) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    /**
     * 获取用户会话数量
     * @param userId
     * @param conversationId
     * @return
     */
    @Select({"select count(id) from ",TABLE_NAME," where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationCount(@Param("userId")int userId,
                             @Param("conversationId")String conversationId);
}
