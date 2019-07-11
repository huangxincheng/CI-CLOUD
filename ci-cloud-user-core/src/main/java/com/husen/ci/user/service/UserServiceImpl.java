package com.husen.ci.user.service;

import com.husen.ci.cache.annotation.DistributedLock;
import com.husen.ci.framework.annotation.PrintMethod;
import com.husen.ci.framework.utils.BeanUtils;
import com.husen.ci.user.dao.UserDao;
import com.husen.ci.user.entity.UserDTO;
import com.husen.ci.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 @Author:MrHuang
 @Date: 2019/6/4 18:02
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    @PrintMethod
    @Override
    @DistributedLock(value = "IUserService.getOneById", expireSecond = 10)
    public User getOneById(String userId) {
        return Optional.ofNullable(userDao.findById(userId))
                .map(uto -> new User().setUserId(uto.getUserId())
                    .setUserName(uto.getUserName())
                    .setUserStatus(uto.getUserStatus())
                    .setUserActiveTime(uto.getUserActiveTime())
                    .setUserCreateTime(uto.getUserCreateTime()))
                .orElse(null);
    }

    @PrintMethod
    @Override
    public User getOneByUserName(String userName) {
        UserDTO dto = userDao.findOneByName(userName);
        return BeanUtils.copy(dto, new User());
    }

    @Override
    public User getOneByUserNameAndPassword(String userName, String password) {
        UserDTO dto = userDao.findOneByNameAndPassword(userName, password);
        return BeanUtils.copy(dto, new User());
    }

    @PrintMethod
    @Override
    public Collection<User> getAll() {
         return Optional.ofNullable(userDao.getAll())
                .orElse(new ArrayList<>())
                .stream().map(uto -> new User().setUserId(uto.getUserId())
                .setUserName(uto.getUserName())
                .setUserStatus(uto.getUserStatus())
                .setUserActiveTime(uto.getUserActiveTime())
                .setUserCreateTime(uto.getUserCreateTime())).collect(Collectors.toList());
    }

    @PrintMethod
    @Override
    public boolean createUser(User user) {
        UserDTO dto = com.husen.ci.framework.utils.BeanUtils.copy(user, new UserDTO());
        UserDTO tmp = userDao.findOneByName(dto.getUserName());
        if (tmp != null) {
            log.info("用户已存在");
            return false;
        }
        boolean flag = userDao.add(dto);
        if (!flag) {
            log.info("创建用户失败");
            return false;
        }
        return true;
    }
}
