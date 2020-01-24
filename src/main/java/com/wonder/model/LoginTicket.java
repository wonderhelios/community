package com.wonder.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author: wonder
 * @Date: 2020/1/2
 */
@Data
public class LoginTicket {
    private int id;
    private int userId;
    private Date expired;
    /**
     * 0:有效
     * 1:无效
     */
    private int status;
    private String ticket;

}
