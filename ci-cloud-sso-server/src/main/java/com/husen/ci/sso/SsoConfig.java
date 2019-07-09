package com.husen.ci.sso;

import com.husen.ci.sso.helper.SsoStoreHelper;
import com.husen.ci.sso.utils.JedisUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/***
 @Author:MrHuang
 @Date: 2019/7/9 10:09
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Configuration
public class SsoConfig implements InitializingBean, DisposableBean {

    @Value("${sso.redis}")
    private String redisAddress;

    @Value("${sso.expire.second}")
    private Integer redisExpireSecond;

    @Override
    public void destroy() throws Exception {
        JedisUtils.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JedisUtils.init(redisAddress);
        SsoStoreHelper.setRedisExpireSecond(redisExpireSecond);
    }
}
