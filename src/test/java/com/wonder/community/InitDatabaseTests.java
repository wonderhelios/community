package com.wonder.community;

import com.wonder.CommunityApplication;
import com.wonder.dao.QuestionDAO;
import com.wonder.dao.UserDAO;
import com.wonder.constant.EntityTypeConst;
import com.wonder.model.Question;
import com.wonder.model.User;
import com.wonder.service.impl.FollowServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;

/**
 * @Author: wonder
 * @Date: 2019/12/31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CommunityApplication.class)
@WebAppConfiguration
public class InitDatabaseTests {

    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    FollowServiceImpl followServiceImpl;

    @Test
    public void contextLoads(){
        Random random = new Random();
        for(int i=1;i<=15;i++){
            User user = new User();
            user.setName(String.format("user%d",i));
            user.setPassword("123");
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
            user.setSalt("");

            userDAO.addUser(user);

            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() +1000 * 3600 * i);
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE{%d}",i));
            question.setContent(String.format("sdfsdfsfsfd Content%d",i));

            questionDAO.addQuestion(question);

        }
//        User user = new User();
//        user.setName(String.format("user5"));
//        user.setPassword("123");
//        user.setHeadUrl(String.format("https://images.wonder.com/head/1t.img"));
//        user.setSalt("");
//        userDAO.addUser(user);
//
//        user.setPassword("blank");
//        userDAO.updatePassword(user);
//        userDAO.deleteById(6);

    }
    @Test
    public void addFollowing(){
        for(int i=0;i<10;i++) {
            for (int j = 1; j < i; j++) {
                followServiceImpl.follow(j, EntityTypeConst.ENTITY_USER,i);
            }
        }
    }
}
