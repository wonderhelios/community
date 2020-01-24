package com.wonder.service.impl;

import com.wonder.dao.LoginTicketDAO;
import com.wonder.dao.UserDAO;
import com.wonder.error.ReloginErrorEnum;
import com.wonder.model.LoginTicket;
import com.wonder.model.User;
import com.wonder.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;

import static com.wonder.error.ReloginErrorEnum.*;

/**
 * @Author: wonder
 * @Date: 2020/1/2
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Override
    public Map<String,Object> register(String username,String password){
        Map<String,Object> map = new HashMap<>(10);

        if(StringUtils.isBlank(username)){
            map.put("msg", USER_NAME_IS_BLANK);
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg",USER_PASSWORD_IS_BLANK);
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msg",USER_HAS_REGISTED);
            return map;
        }

        user = new User();
        user.setName(username);
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(DigestUtils.md5DigestAsHex((password + user.getSalt()).getBytes()));
        userDAO.addUser(user);

        //登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }
    @Override
    public Map<String,Object> login(String username,String password){
        Map<String,Object> map = new HashMap<>(10);

        if(StringUtils.isBlank(username)){
            map.put("msg", USER_NAME_IS_BLANK);
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg",ReloginErrorEnum.USER_PASSWORD_IS_BLANK);
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user == null){
            map.put("msg",ReloginErrorEnum.USER_NAME_IS_ERROR);
            return map;
        }
        if(!DigestUtils.md5DigestAsHex((password + user.getSalt()).getBytes()).equals(user.getPassword())){
            map.put("msg",ReloginErrorEnum.USER_PASSWORD_IS_ERROR);
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }
    private String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 3600 * 24 * 100);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));

        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }
    @Override
    public User selectUserById(int id){
        return userDAO.selectById(id);
    }
    @Override
    public User selectUserByName(String name){
        return userDAO.selectByName(name);
    }
    @Override
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }

}
