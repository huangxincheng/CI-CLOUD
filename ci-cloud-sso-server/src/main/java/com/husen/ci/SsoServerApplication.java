package com.husen.ci;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/***
 @Author:MrHuang
 @Date: 2019/7/8 21:48
 @DESC: TODO
 @VERSION: 1.0
 ***/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class SsoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoServerApplication.class, args);
    }
}
