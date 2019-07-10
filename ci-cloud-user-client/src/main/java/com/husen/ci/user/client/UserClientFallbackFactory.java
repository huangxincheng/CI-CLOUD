package com.husen.ci.user.client;

import com.husen.ci.user.pojo.User;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/***
 @Author:MrHuang
 @Date: 2019/7/5 10:49
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Component
public class UserClientFallbackFactory implements FallbackFactory<IUserClient> {

    @Override
    public IUserClient create(Throwable throwable) {
        return new IUserClient() {
            @Override
            public User getUserById(String userId) {
                return null;
            }

            @Override
            public User getUserByName(String userName) {
                return null;
            }

            @Override
            public User getUserByNameAndPassword(String userName, String password) {
                return null;
            }

            @Override
            public boolean saveUser(User user) {
                return false;
            }
        };
    }
}
