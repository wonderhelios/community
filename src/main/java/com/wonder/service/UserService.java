package com.wonder.service;

import com.wonder.model.LoginTicket;
import com.wonder.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.*;

/**
 * @Author: wonder
 * @Date: 2020/1/24
 */
public interface UserService {
    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    Map<String,Object> register(String username, String password);

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    Map<String,Object> login(String username,String password);

    /**
     * 根据用户id获取用户
     * @param id
     * @return
     */
    User selectUserById(int id);

    /**
     * 根据用户名获取用户
     * @param name
     * @return
     */
    User selectUserByName(String name);

    /**
     * 注销用户
     * @param ticket
     */
    void logout(String ticket);

}
