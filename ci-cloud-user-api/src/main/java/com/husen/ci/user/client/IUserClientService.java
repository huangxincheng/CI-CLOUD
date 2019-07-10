package com.husen.ci.user.client;

import com.husen.ci.user.pojo.User;
import org.springframework.web.bind.annotation.*;

/***
 @Author:MrHuang
 @Date: 2019/7/5 10:48
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RequestMapping("/client/v1/user")
public interface IUserClientService {

    @RequestMapping("/getById/{userId}")
    User getUserById(@PathVariable("userId") String userId);

    @RequestMapping("/getByName/{userName}")
    User getUserByName(@PathVariable("userName") String userName);

    @GetMapping("/getByNameAndPassword")
    User getUserByNameAndPassword(@RequestParam("userName") String userName, @RequestParam("password") String password);

    @RequestMapping("/saveUser")
    boolean saveUser(@RequestBody User user);
}
