package com.wonder.controller;

import com.wonder.service.WonderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author: wonder
 * @Date: 2019/12/30
 */
@Controller
public class SettingController {
    @Autowired
    WonderService wonderService;

    @RequestMapping(path = "/setting", method = {RequestMethod.GET})
    @ResponseBody
    public String setting(HttpSession httpSession) {
        return "Setting ok " + wonderService.getMessage(1);
    }
}
