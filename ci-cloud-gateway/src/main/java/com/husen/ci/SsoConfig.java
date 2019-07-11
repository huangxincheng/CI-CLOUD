package com.husen.ci;

import com.husen.ci.sso.AuthGatewayFilterFactory;
import com.husen.ci.sso.utils.JedisUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 @Author:MrHuang
 @Date: 2019/7/11 15:50
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Configuration
public class SsoConfig implements InitializingBean, DisposableBean {

    @Bean
    public AuthGatewayFilterFactory authGatewayFilterFactory() {
        return new AuthGatewayFilterFactory();
    }

    @Value("${sso.redis}")
    private String ssoRedisAddress;

    @Override
    public void destroy() throws Exception {
        JedisUtils.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JedisUtils.init(ssoRedisAddress);
    }
}
