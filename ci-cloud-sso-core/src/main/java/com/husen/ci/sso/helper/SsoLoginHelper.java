package com.husen.ci.sso.helper;

import com.husen.ci.framework.auth.JwtUtils;
import com.husen.ci.sso.session.SsoSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/***
 @Author:MrHuang
 @Date: 2019/7/9 10:43
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
public class SsoLoginHelper {

    /**
     * 生成TokenSessionId
     * @param userId
     * @return
     */
    public static String createTokenSessionId(String userId) {
        return JwtUtils.encode(userId);
    }

    /**
     * 校验TokenSessionId 校验失败抛出异常
     * @param tokenSessionId
     */
    private static void checkTokenSessionIdWithThrow(String tokenSessionId) {
        if (StringUtils.isEmpty(tokenSessionId)) {
            throw new IllegalArgumentException("凭证不能为空");
        }
        if (!JwtUtils.checkToken(tokenSessionId)) {
            throw new IllegalArgumentException("凭证校验失败");
        }
    }

    /**
     * 校验TokenSessionId 校验失败 返回false
     * @param tokenSessionId
     * @return
     */
    private static boolean checkTokenSessionIdWithBoolean(String tokenSessionId) {
        if (StringUtils.isEmpty(tokenSessionId)) {
            return false;
        }
        return JwtUtils.checkToken(tokenSessionId);
    }

    /**
     * 登陆方法
     * @param tokenSessionId
     */
    public static void login(String tokenSessionId) {
        checkTokenSessionIdWithThrow(tokenSessionId);
        String userId = JwtUtils.decode(tokenSessionId);
        SsoSession session = new SsoSession()
                .setUserId(userId)
                .setTokenSessionId(tokenSessionId)
                .setFreshTime(System.currentTimeMillis())
                .setExpireSecond(SsoStoreHelper.getRedisExpireSecond());
        SsoStoreHelper.put(userId, session);
    }

    /**
     * 登出方法
     * @param tokenSessionId
     */
    public static void logout(String tokenSessionId) {
        checkTokenSessionIdWithThrow(tokenSessionId);
        String userId = JwtUtils.decode(tokenSessionId);
        SsoStoreHelper.remove(userId);
    }


    /**
     * 登陆检查方法
     * @param tokenSessionId
     * @return
     */
    public static SsoSession loginCheck(String tokenSessionId) {
        try {
            boolean check = checkTokenSessionIdWithBoolean(tokenSessionId);
            if (!check) {
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