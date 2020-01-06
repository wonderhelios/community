package com.wonder.model;

import org.springframework.stereotype.Component;

/**
 * @Author: wonder
 * @Date: 2020/1/5
 */

@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }
    public void setUser(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();;
    }
}
