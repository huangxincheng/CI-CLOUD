package com.husen.ci.user.service;

import com.husen.ci.framework.utils.IpUtils;
import com.husen.ci.user.entity.UserDTO;
import com.husen.ci.user.pojo.User;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 @Author:MrHuang
 @Date: 2019/6/25 14:59
 @DESC: TODO
 @VERSION: 1.0
 ***/
public interface IUserService {

     User getOneById(String userId);

     User getOneByUserName(String userName);

     Collection<User> getAll();

     boolean createUser(User user);
}
