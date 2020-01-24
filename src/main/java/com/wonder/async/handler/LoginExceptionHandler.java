package com.wonder.async.handler;

import com.wonder.async.EventHandler;
import com.wonder.async.EventModel;
import com.wonder.async.EventType;
import com.wonder.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Author: wonder
 * @Date: 2020/1/16
 */
@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    private MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        /**
        //判断为异常登录
        Map<String,Object> map = new HashMap<>();
        map.put("username",model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"),
                "登录异常","mails/login_exception.html",map);
         **/
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
