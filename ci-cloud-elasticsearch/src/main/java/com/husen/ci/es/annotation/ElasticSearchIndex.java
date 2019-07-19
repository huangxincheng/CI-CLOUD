package com.husen.ci.es.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 @Author:MrHuang
 @Date: 2019/7/18 17:31
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSearchIndex {

    /**
     * index Name
     * @return
     */
    @AliasFor("index")
    String value() default "";

    @AliasFor("value")
    String index() default "";
}
