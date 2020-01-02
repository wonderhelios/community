package com.wonder.service;

import com.wonder.dao.UserDAO;
import com.wonder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: blank
 * @Date: 2020/1/2
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }
}
