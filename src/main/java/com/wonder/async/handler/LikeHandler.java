package com.wonder.async.handler;

import com.wonder.async.EventHandler;
import com.wonder.async.EventModel;
import com.wonder.async.EventType;
import com.wonder.constant.UserConst;
import com.wonder.model.Message;
import com.wonder.model.User;
import com.wonder.service.impl.MessageServiceImpl;
import com.wonder.service.impl.UserServiceImpl;
import com.wonder.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/14
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    private MessageServiceImpl messageServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(UserConst.ANNOYMOUS_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userServiceImpl.selectUserById(model.getActorId());
        message.setContent("用户" + user.getId()
                            + "赞了你的评论:http://127.0.0.1:8080/question/"
                            + model.getExt("questionId"));
        messageServiceImpl.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
