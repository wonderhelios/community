package com.wonder.service;

import com.wonder.model.Question;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/24
 */
public interface QuestionService {

    /**
     * 选取最新的问题
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Question> getLatestQuestion(int userId, int offset, int limit);

    /**
     * 添加问题
     * @param question
     * @return
     */
    public int addQuestion(Question question);

    /**
     * 更新评论数量
     * @param id
     * @param commentCount
     * @return
     */
    public int updateCommentCount(int id,int commentCount);

    /**
     * 根据问题id获取问题
     * @param id
     * @return
     */
    public Question getQuestionById(int id);
}
