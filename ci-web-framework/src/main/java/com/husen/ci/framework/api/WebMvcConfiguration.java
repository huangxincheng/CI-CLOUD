package com.husen.ci.framework.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/***
 @Author:MrHuang
 @Date: 2019/6/24 11:30
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    /**
     * 添加拦截器
      * @param registry 拦截器链
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebReqeustInterceptor());
    }
}
