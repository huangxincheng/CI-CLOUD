package com.husen.ci.cache.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 @Author:MrHuang
 @Date: 2019/7/11 11:08
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    @AliasFor("lockKey")
    String value();

    @AliasFor("value")
    String lockKey() default "";

    String clientId() default "";

    int expireSecond() default 3;

    long blockMilliSecond() default 0;

    long sleppMilliSecond() default 0;
}
