package com.wonder.service;

import com.wonder.dao.QuestionDAO;
import com.wonder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/2
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public List<Question> getLatestQuestion(int userId,int offset,int limit){
        return questionDAO.selectLatestQuestions(userId,offset,limit);
    }
}
