package com.husen.ci.user.client;

import com.husen.ci.user.pojo.User;
import com.husen.ci.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/***
 @Author:MrHuang
 @Date: 2019/7/5 10:54
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RestController
public class UserClientServiceImpl implements IUserClientService {

    @Autowired
    private IUserService userService;

    @Override
    public User getUserById(String userId) {
        return userService.getOneById(userId);
    }

    @Override
    public User getUserByName(String userName) {
        return userService.getOneByUserName(userName);
    }

    @Override
    public User getUserByNameAndPassword(String userName, String password) {
        return userService.getOneByUserNameAndPassword(userName, password);
    }

    @Override
    public boolean saveUser(User user) {
        return userService.createUser(user);
    }
}
