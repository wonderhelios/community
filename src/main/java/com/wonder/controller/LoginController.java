package com.wonder.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.wonder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: wonder
 * @Date: 2020/1/2
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping(path = "/reg/",method = RequestMethod.POST)
    public String reg(Model model,
                      @RequestParam("username")String username,
                      @RequestParam("password")String password,
                      @RequestParam(value = "remember",defaultValue = "false")boolean rememberme,
                      HttpServletResponse response){
        try{
            Map<String,Object> map = userService.register(username,password);
            if(map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600 * 24 * 7);
                }
                response.addCookie(cookie);
                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("注册异常:" + e.getMessage());
            model.addAttribute("msg","服务器异常");
            return "login";
        }
    }
    @RequestMapping(path = {"/reglogin"},method = RequestMethod.GET)
    public String regloginPage(Model model){
        return "login";
    }

    @RequestMapping(path = {"/login/"},method = RequestMethod.POST)
    public String login(Model model, @RequestParam("username")String username,
                        @RequestParam("password")String password,
                        @RequestParam(value = "rememberme",defaultValue = "false")boolean rememberme,
                        HttpServletResponse response){
        try{
            Map<String,Object> map = userService.login(username,password);

            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600 * 24 * 7);
                }
                response.addCookie(cookie);
                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }

        }catch (Exception e){
            logger.error("登录异常:" + e.getMessage());
            return "login";
        }
    }
    @RequestMapping(path = {"/logout"},method = {RequestMethod.GET,RequestMethod.GET})
    public String logout(@CookieValue("ticket")String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }
}