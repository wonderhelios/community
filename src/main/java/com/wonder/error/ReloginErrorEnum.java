package com.wonder.error;

/**
 * 用来封装注册和登录的错误信息
 *
 * @Author: wonder
 * @Date: 2020/1/24
 */
public enum ReloginErrorEnum {
    USER_NAME_IS_BLANK(20201, "用户名不能为空"),
    USER_PASSWORD_IS_BLANK(20202,"密码不能为空"),

    USER_NAME_IS_ERROR(20203,"用户名错误"),
    USER_PASSWORD_IS_ERROR(20204,"密码错误"),

    USER_HAS_REGISTED(20205,"用户已经被注册");

    private int code;
    private String msg;

    private ReloginErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public int getCode(){
        return this.code;
    }
}
