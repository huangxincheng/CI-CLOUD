package com.husen.ci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/***
 @Author:MrHuang
 @Date: 2019/6/25 13:06
 @DESC: TODO
 @VERSION: 1.0

 @SpringBootApplication SpringBoot启动类注解
 @EnableDiscoveryClient 开启服务发现，服务注册注解
 @EnableFeignClients 开启Feign调用注解
 @EnableCircuitBreaker 开启Hystrix熔断器注解
 @EnableAspectJAutoProxy 开启Aop注解
 ***/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
 }
