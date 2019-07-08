package com.husen.ci.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 @Author:MrHuang 打印方法耗时及方法参数和返回值的注解
 @Date: 2019/7/8 18:25
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrintMethod {


}
