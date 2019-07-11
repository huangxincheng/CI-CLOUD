package com.husen.ci;

import com.husen.ci.cache.DistributedLockUtils;
import com.husen.ci.cache.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/***
 @Author:MrHuang
 @Date: 2019/7/11 10:18
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {



    @Test
    public void getSet() {
        String a123 = RedisUtils.getForString("a123");
        System.out.println(a123);
        RedisUtils.setForString("a123", "a456");
        String a1231 = RedisUtils.getForString("a123");
        System.out.println(a1231);
    }

    @Test
    public void lock() {
        DistributedLockUtils.tryLockWithNotBlock("asd", "123", 100, () -> {
            System.out.println("aaaa");
            System.out.println("aaaa");
            System.out.println("aaaa");
        });
    }
}
