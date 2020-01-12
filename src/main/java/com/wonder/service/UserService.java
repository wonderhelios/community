package com.wonder.service;

import com.wonder.dao.LoginTicketDAO;
import com.wonder.dao.UserDAO;
import com.wonder.model.LoginTicket;
import com.wonder.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import sun.security.krb5.internal.Ticket;

import java.util.*;

/**
 * @Author: wonder
 * @Date: 2020/1/2
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    public Map<String,Object> register(String username,String password){
        Map<String,Object> map = new HashMap<>();

        if(StringUtils.isBlank(username)){
            map.put("msg","用户名为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msg","用户已被注册");
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
    public Map<String,Object> login(String username,String password){
        Map<String,Object> map = new HashMap<>();

        if(StringUtils.isBlank("username")){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank("password")){
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user == null){
            map.put("msg","用户不存在");
            return map;
        }
        if(!DigestUtils.md5DigestAsHex((password + user.getSalt()).getBytes()).equals(user.getPassword())){
            map.put("msg","密码错误");
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
    public User selectUserById(int id){
        return userDAO.selectById(id);
    }
    public User selectUserByName(String name){
        return userDAO.selectByName(name);
    }
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }

}
