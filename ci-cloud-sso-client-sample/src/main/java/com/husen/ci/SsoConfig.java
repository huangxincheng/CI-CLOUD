package com.husen.ci;

import com.husen.ci.sso.ClientConf;
import com.husen.ci.sso.filter.SsoTokenFilter;
import com.husen.ci.sso.utils.JedisUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

/***
 @Author:MrHuang
 @Date: 2019/7/9 15:08
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Configuration
public class SsoConfig implements InitializingBean, DisposableBean {

    @Value("${sso.server}")
    private String ssoServer;

    @Value("${sso.redis}")
    private String ssoRedisAddress;

    @Value("${sso.excluded.paths}")
    private String ssoExcludedPaths;

//    @Bean
    public FilterRegistrationBean ssoFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("ssoFilter");
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setFilter(new SsoTokenFilter());
        registrationBean.addInitParameter(ClientConf.SSO_SERVER, ssoServer);
        registrationBean.addInitParameter(ClientConf.SSO_EXCLUDED_PATHS, ssoExcludedPaths);
        return registrationBean;
    }

    @Override
    public void destroy() throws Exception {
        JedisUtils.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JedisUtils.init(ssoRedisAddress);
    }
}
