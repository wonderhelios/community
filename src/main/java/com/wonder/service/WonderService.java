package com.wonder.service;

import org.springframework.stereotype.Service;

/**
 * @Author: wonder
 * @Date: 2019/12/30
 */
@Service
public class WonderService {
    public String getMessage(int userId){
        return "Hello Message" + String.valueOf(userId);
    }
}
