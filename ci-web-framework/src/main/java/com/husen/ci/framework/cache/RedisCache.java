package com.husen.ci.framework.cache;

import com.husen.ci.framework.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/***
 @Author:MrHuang
 @Date: 2019/6/21 15:05
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
public class RedisCache {

    private static StringRedisTemplate template;

    private static ValueOperations<String, String> vos = template.opsForValue();

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate template) {
        RedisCache.template = template;
    }

    public static String getToString(String key) {
        try {
            return vos.get(key);
        } catch (Exception e) {
            log.error("RedisUtil get", e);
            return null;
        }
    }

    public static <T> T getToObject(String key, Class<T> clazz) {
        return JSONUtils.json2Bean(getToString(key), clazz);
    }

    public static <T> List<T> getToArray(String key, Class<T> clazz) {
        return JSONUtils.json2List(getToString(key), clazz);
    }


    public static boolean set(String key, String value) {
        try {
            vos.set(key, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil set", e);
            return false;
        }
    }

    public static boolean set(String key, Object value) {
        return set(key, JSONUtils.object2Json(value));
    }

    public static boolean setex(String key, String value, int expire) {
        try {
            vos.set(key, value, expire, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil set", e);
            return false;
        }
    }

    public static boolean setex(String key, Object value, int expire) {
        return setex(key, JSONUtils.object2Json(value), expire);
    }

    public static boolean setnx(String key, String value) {
        try {
            return vos.setIfAbsent(key, value);
        } catch (Exception e) {
            log.error("RedisUtil setnx", e);
            return false;
        }
    }

    public static Long incr(String key, long delta) {
        try {
            return vos.increment(key, delta);
        } catch (Exception e) {
            log.error("RedisUtil incr", e);
            return null;
        }
    }


    public static Double incr(String key, double delta) {
        try {
            return vos.increment(key, delta);
        } catch (Exception e) {
            log.error("RedisUtil incr", e);
            return null;
        }
    }

    public static Long decr(String key, long delta) {
        return incr(key, -delta);
    }


    public static Double decr(String key, double delta) {
        return incr(key, -delta);
    }


    public static List<String> mget(String... keys) {
        return mget(Arrays.asList(keys));
    }

    public static List<String> mget(Collection<String> keys) {
        try {
            return vos.multiGet(keys);
        } catch (Exception e) {
            log.error("RedisUtil mget", e);
            return null;
        }
    }

    public static boolean mset(Map<String, String> map) {
        try {
            vos.multiSet(map);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil mset", e);
            return false;
        }
    }

    public static String getSet(String key, String value) {
        try {
            return vos.getAndSet(key, value);
        } catch (Exception e) {
            log.error("RedisUtil getSet", e);
            return null;
        }
    }


    public static Long size(String key) {
        try {
            return vos.size(key);
        } catch (Exception e) {
            log.error("RedisUtil size", e);
            return null;
        }
    }

    public static Boolean msetnx(Map<String,String> map) {
        try {
            return vos.multiSetIfAbsent(map);
        } catch (Exception e) {
            log.error("RedisUtil msetnx", e);
            return null;
        }
    }
}
