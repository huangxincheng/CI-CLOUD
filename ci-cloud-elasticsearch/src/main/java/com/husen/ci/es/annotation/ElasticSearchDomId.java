package com.husen.ci.es.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 @Author:MrHuang
 @Date: 2019/7/18 18:18
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSearchDomId {


}
