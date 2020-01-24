package com.wonder.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author: wonder
 * @Date: 2019/12/31
 */
@Data
public class Question {
    private int id;
    private String title;
    private String content;
    private Date createdDate;
    private int userId;
    private int commentCount;
}