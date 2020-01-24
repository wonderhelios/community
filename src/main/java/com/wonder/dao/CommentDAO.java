package com.wonder.dao;

import com.wonder.model.Comment;
import com.wonder.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/10
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";

    String INSERT_FIELDS = " user_id, created_date, entity_id, entity_type, content, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 添加评论到数据库
     * @param comment
     * @return
     */
    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,
            " ) values (#{userId},#{createdDate},#{entityId},#{entityType},#{content},#{status})"})
    int addComment(Comment comment);

    /**
     * 获取评论数量
     * @param entityId
     * @param entityType
     * @return
     */
    @Select({"select count(id) from ",TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId")int entityId,
                        @Param("entityType")int entityType);

    /**
     * 根据用户id获取评论数
     * @param userId
     * @return
     */
    @Select({"select count(id) from ",TABLE_NAME,
            " where user_id=#{userId}"})
    int getUserCommentCount(@Param("userId")int userId);

    /**
     * 根据实体类型获取评论
     * @param entityId
     * @param entityType
     * @return
     */
    @Select({"select * from ",TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType}"})
    List<Comment> getCommentByEntity(@Param("entityId")int entityId,
                                     @Param("entityType")int entityType);

    /**
     * 根据评论id获取评论
     * @param commentId
     * @return
     */
    @Select({"select * from ",TABLE_NAME,
            " where id=#{commentId}"})
    Comment getCommentById(@Param("commentId")int commentId);
}