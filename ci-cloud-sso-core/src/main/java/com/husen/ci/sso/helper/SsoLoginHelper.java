package com.husen.ci.sso.helper;

import com.husen.ci.framework.auth.JwtUtils;
import com.husen.ci.sso.session.SsoSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/***
 @Author:MrHuang
 @Date: 2019/7/9 10:43
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
public class SsoLoginHelper {

    public static String createTokenSessionId(String userId) {
        return JwtUtils.encode(userId);
    }

    private static void checkTokenSessionId(String tokenSessionId) {
        Assert.notNull(tokenSessionId, "tokenSessionId not Null");
        if (!JwtUtils.checkToken(tokenSessionId)) {
            Assert.notNull(tokenSessionId, "tokenSessionId check fail");
        }
    }

    public static void login(String tokenSessionId) {
        checkTokenSessionId(tokenSessionId);
        String userId = JwtUtils.decode(tokenSessionId);
        SsoSession session = new SsoSession()
                .setUserId(userId)
                .setTokenSessionId(tokenSessionId)
                .setFreshTime(System.currentTimeMillis())
                .setExpireSecond(SsoStoreHelper.getRedisExpireSecond());
        SsoStoreHelper.put(userId, session);
    }

    public static void logout(String tokenSessionId) {
        checkTokenSessionId(tokenSessionId);
        String userId = JwtUtils.decode(tokenSessionId);
        SsoStoreHelper.remove(userId);
    }


    public static SsoSession loginCheck(String tokenSessionId) {
        try {
            if (StringUtils.isEmpty(tokenSessionId)) {
                return null;
            }
            if (!JwtUtils.checkToken(tokenSessionId)) {
                return null;
            }
            String userId = JwtUtils.decode(tokenSessionId);
            if (!StringUtils.isEmpty(userId)) {
                SsoSession ssoSession = SsoStoreHelper.get(userId);
                if (ssoSession != null) {
                    // 自动刷新tokenSession时间
                    if (System.currentTimeMillis() >= (ssoSession.getFreshTime() + SsoStoreHelper.getRedisExpireSecond() * 1000 / 2)) {
                        ssoSession.setFreshTime(System.currentTimeMillis());
                        SsoStoreHelper.put(userId, ssoSession);
                    }
                }
                return ssoSession;
            }
        } catch (Exception e) {
            log.error("loginCheck fail", e);
        }
        return null;
    }


}