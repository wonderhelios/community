package com.wonder.service;

import com.wonder.model.Comment;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/24
 */
public interface CommentService {
    /**
     * 添加评论
     * @param comment
     * @return
     */
    int addComment(Comment comment);

    /**
     * 获取评论数量
     * @param entityId
     * @param entityType
     * @return
     */
    int getCommentCount(int entityId, int entityType);

    /**
     * 根据用户id获取评论数
     * @param userId
     * @return
     */
    int getUserCommentCount(int userId);

    /**
     * 根据实体类型获取评论
     * @param entityId
     * @param entityType
     * @return
     */
    List<Comment> getCommentByEntity(int entityId, int entityType);

    /**
     * 根据评论id获取评论
     * @param commentId
     * @return
     */
    Comment getCommentById(int commentId);
}
