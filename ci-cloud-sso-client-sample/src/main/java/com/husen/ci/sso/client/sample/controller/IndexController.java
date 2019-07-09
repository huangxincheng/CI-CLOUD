package com.husen.ci.sso.client.sample.controller;

import com.husen.ci.framework.api.GlobalApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 @Author:MrHuang
 @Date: 2019/7/9 15:08
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RestController
@RequestMapping("/app")
public class IndexController {

    @RequestMapping
    public GlobalApiResponse index() {
        return GlobalApiResponse.toSuccess("Index");
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }

}
