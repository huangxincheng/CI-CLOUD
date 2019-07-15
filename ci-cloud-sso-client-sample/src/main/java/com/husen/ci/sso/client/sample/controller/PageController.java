package com.husen.ci.sso.client.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/***
 @Author:MrHuang
 @Date: 2019/7/15 10:36
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RequestMapping("/page")
@Controller
public class PageController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
}
