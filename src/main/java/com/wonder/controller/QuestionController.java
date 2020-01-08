package com.wonder.controller;

import com.wonder.dao.QuestionDAO;
import com.wonder.model.HostHolder;
import com.wonder.model.Question;
import com.wonder.service.QuestionService;
import com.wonder.util.WonserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Author: wonder
 * @Date: 2020/1/7
 */
@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/question/add",method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title")String title,
                              @RequestParam("content")String content){
        try{
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if(hostHolder.getUser() == null){
//                question.setUserId(WonserUtils.ANNOYMOUS_USERID);
                return WonserUtils.getJSONString(999);
            }else{
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question) > 0){
                return WonserUtils.getJSONString(0);
            }
            questionService.addQuestion(question);

        }catch (Exception e){
            logger.error("增加问题失败:" + e.getMessage());
        }
        return WonserUtils.getJSONString(1,"失败");
    }
}
