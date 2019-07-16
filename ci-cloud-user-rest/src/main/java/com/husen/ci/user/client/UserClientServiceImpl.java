package com.husen.ci.user.client;

import com.husen.ci.user.pojo.User;
import com.husen.ci.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/***
 @Author:MrHuang
 @Date: 2019/7/5 10:54
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RestController
@Api(tags = "用户相关FeignClient")
public class UserClientServiceImpl implements IUserClientService {

    @Autowired
    private IUserService userService;

    @ApiOperation(value="获取用户详细信息", notes="根据userId获取用户详细信息")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String", paramType = "path")
    @Override
    public User getUserById(String userId) {
        return userService.getOneById(userId);
    }

    @ApiOperation(value="获取用户详细信息", notes="根据userId获取用户详细信息")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @Override
    public User getUserByName(String userName) {
        return userService.getOneByUserName(userName);
    }

    @Override
    public User getUserByNameAndPassword(@ApiParam(name = "userName", value = "用户名", required = true) String userName,
                                         @ApiParam(name = "password", value = "密码", required = true) String password) {
        return userService.getOneByUserNameAndPassword(userName, password);
    }

    @ApiOperation(value="创建用户", notes="保存用户信息")
    @ApiImplicitParam(name = "user", value = "用户信息", required = true, dataType = "User")
    @Override
    public boolean saveUser(User user) {
        return userService.createUser(user);
    }
}
