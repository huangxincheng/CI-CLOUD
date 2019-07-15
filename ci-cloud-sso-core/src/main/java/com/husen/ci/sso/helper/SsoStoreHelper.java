package com.husen.ci.sso.helper;

import com.husen.ci.framework.json.JSONUtils;
import com.husen.ci.sso.session.SsoSession;
import com.husen.ci.sso.utils.JedisUtils;

/***
 @Author:MrHuang
 @Date: 2019/7/9 10:13
 @DESC: TODO
 @VERSION: 1.0
 ***/
public final class SsoStoreHelper {

    /**
     * 86400 second, 24 hour
     */
    private static int REDIS_EXPIRE_SECOND = 86400;

    /**
     * STORE_KEY PREFIX
     */
    private static final String STORE_KEY_PRE = "STORE_KEY:";

    /**
     * REDIS_KEY
     * @param storeKey
     * @return
     */
    private static String redisKey(String storeKey) {
        return STORE_KEY_PRE + storeKey;
    }

    public static void setRedisExpireSecond(int redisExpireSecond) {
        SsoStoreHelper.REDIS_EXPIRE_SECOND = redisExpireSecond;
    }

    public static int getRedisExpireSecond() {
        return SsoStoreHelper.REDIS_EXPIRE_SECOND;
    }

    /**
     * 从缓存中获取ssoSession
     * @param storeKey
     * @return
     */
    public static SsoSession get(String storeKey) {
        String redisKey = redisKey(storeKey);
        String stringValue = JedisUtils.getStringValue(redisKey);
        return JSONUtils.json2Bean(stringValue, SsoSession.class);
    }

    /**
     * 设置ssoSession到缓存中
     * @param storeKey
     * @param ssoSession
     */
    public static void put(String storeKey, SsoSession ssoSession) {
        String redisKey = redisKey(storeKey);
        JedisUtils.setStringValue(redisKey, JSONUtils.object2Json(ssoSession), REDIS_EXPIRE_SECOND);
    }

    /**
     * 从缓存中删除ssoSession
     * @param storeKey
     */
    public static void remove(String storeKey) {
        String redisKey = redisKey(storeKey);
        JedisUtils.del(redisKey);
    }
}
