package com.husen.ci;

import com.husen.ci.framework.utils.AsyncExecutor;
import com.husen.ci.user.pojo.User;
import com.husen.ci.user.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/***
 @Author:MrHuang
 @Date: 2019/7/8 18:47
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPrintMethod {

    @Autowired
    private IUserService userService;

    @Test
    public void print() {
        User oneById = userService.getOneById("5d1efabfc607690006f84d07");
        System.out.println(oneById);
        User oneById2 = userService.getOneById("5d1efabfc607690006f84d07");
        System.out.println(oneById2);
        for (int i = 1; i <= 10; i++) {
            AsyncExecutor.execute(() -> {
                while (true) {
                    User us = userService.getOneById("5d1efabfc607690006f84d07");
                    System.out.println(us);
                }
            });
        }
        while (true) {

        }
    }
}
