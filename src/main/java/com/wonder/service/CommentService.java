package com.wonder.service;

import com.sun.deploy.net.HttpUtils;
import com.wonder.dao.CommentDAO;
import com.wonder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/10
 */
@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    SensitiveService sensitiveService;

    public int addComment(Comment comment){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));

        return commentDAO.addComment(comment) > 0 ? comment.getUserId() : 0;
    }
    public int getCommentCount(int entityId,int entityType){
        return commentDAO.getCommentCount(entityId,entityType);
    }
    public List<Comment> getCommentByEntity(int entityId,int entityType){
        return commentDAO.getCommentByEntity(entityId,entityType);
    }
}
