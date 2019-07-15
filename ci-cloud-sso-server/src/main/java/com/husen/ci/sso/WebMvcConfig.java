package com.husen.ci.sso;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author husen
 * addCorsMappings 配置CORS跨域问题1
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
                 // 配置CORS允许访问mappign路径
        registry.addMapping("/**")
                // 配置允许访问的HTTP方法 .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedMethods("*")
                // 配置允许访问的请求头
                .allowedHeaders("*")
                .maxAge(1800)
                // 配置允许访问的客户端ip .allowedOrigins("http://localhost:30084")
                .allowedOrigins("*")
                .allowCredentials(true);
    }
}