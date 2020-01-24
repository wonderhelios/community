package com.wonder.service.impl;

import com.wonder.dao.CommentDAO;
import com.wonder.model.Comment;
import com.wonder.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/10
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private SensitiveService sensitiveService;

    @Override
    public int addComment(Comment comment){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));

        return commentDAO.addComment(comment) > 0 ? comment.getUserId() : 0;
    }
    @Override
    public int getCommentCount(int entityId, int entityType){
        return commentDAO.getCommentCount(entityId,entityType);
    }

    @Override
    public int getUserCommentCount(int userId){
        return commentDAO.getUserCommentCount(userId);
    }

    @Override
    public List<Comment> getCommentByEntity(int entityId, int entityType){
        return commentDAO.getCommentByEntity(entityId,entityType);
    }
    @Override
    public Comment getCommentById(int commentId){
        return commentDAO.getCommentById(commentId);
    }

}
