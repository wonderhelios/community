package com.wonder.service.impl;

import com.wonder.dao.QuestionDAO;
import com.wonder.model.Question;
import com.wonder.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/2
 */
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private SensitiveService sensitiveService;

    @Override
    public List<Question> getLatestQuestion(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    @Override
    public int addQuestion(Question question) {
        //敏感词过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));

        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));

        return questionDAO.addQuestion(question) > 0 ? question.getUserId() : 0;
    }

    @Override
    public int updateCommentCount(int id, int commentCount) {
        return questionDAO.updateCommentCount(id, commentCount);
    }

    @Override
    public Question getQuestionById(int id) {
        return questionDAO.selectQuestionById(id);
    }
}
