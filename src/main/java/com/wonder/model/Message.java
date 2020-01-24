package com.wonder.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author: wonder
 * @Date: 2020/1/11
 */
@Data
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String content;
    private Date createdDate;
    private int hasRead;
    private String conversationId;
}
