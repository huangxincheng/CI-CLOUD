package com.husen.ci.user.service;

import com.husen.ci.user.pojo.User;

import java.util.Collection;

/***
 @Author:MrHuang
 @Date: 2019/6/25 14:59
 @DESC: TODO
 @VERSION: 1.0
 ***/
public interface IUserService {

     User getOneById(String userId);

     User getOneByUserName(String userName);

     User getOneByUserNameAndPassword(String userName, String password);

     Collection<User> getAll();

     boolean createUser(User user);
}
