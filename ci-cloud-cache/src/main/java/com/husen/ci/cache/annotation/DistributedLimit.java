package com.husen.ci.cache.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 @Author:MrHuang
 @Date: 2019/7/24 17:15
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLimit {

    @AliasFor("limitKey")
    String value() default "";

    @AliasFor("value")
    String limitKey() default "";

    int limitNum() default 1000;

    int expireSecond() default 1;
}
