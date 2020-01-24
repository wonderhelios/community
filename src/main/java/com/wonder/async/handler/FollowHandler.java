package com.wonder.async.handler;

import com.wonder.async.EventHandler;
import com.wonder.async.EventModel;
import com.wonder.async.EventType;
import com.wonder.constant.UserConst;
import com.wonder.constant.EntityTypeConst;
import com.wonder.model.Message;
import com.wonder.model.User;
import com.wonder.service.impl.MessageServiceImpl;
import com.wonder.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/21
 */
@Component
public class FollowHandler implements EventHandler {
    @Autowired
    private MessageServiceImpl messageServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(UserConst.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userServiceImpl.selectUserById(model.getActorId());

        if (model.getEntityType() == EntityTypeConst.ENTITY_QUESTION) {
            message.setContent("用户" + user.getName()
                    + "关注了你的问题,http://127.0.0.1:8080/question/" + model.getEntityId());
        } else if (model.getEntityType() == EntityTypeConst.ENTITY_USER) {
            message.setContent("用户" + user.getName()
                    + "关注了你,http://127.0.0.1:8080/user/" + model.getActorId());
        }

        messageServiceImpl.addMessage(message);
    }
    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
