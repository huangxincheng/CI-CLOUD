package com.husen.ci.sso;

import com.husen.ci.framework.api.WebMvcConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * @Author husen
 */
@Configuration
public class WebMvcConfig extends WebMvcConfiguration {

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