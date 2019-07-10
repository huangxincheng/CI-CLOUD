package com.husen.ci.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/***
 @Author:MrHuang
 @Date: 2019/7/10 18:35
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
public class RedisLockUtils {

    @FunctionalInterface
    public interface  RedisLockCall {
        void call();
    }

    /**
     * 获取不阻塞的锁成功返回：1
     */
    private static final Long LOCK_SUCCESS = 1L;

    /**
     * 获取不阻塞的锁lua脚本
     */
    private static final String NO_BLOCK_LOCK_SCRIPT = "if redis.call('set', KEYS[1], ARGV[1], 'NX', 'EX', ARGV[2]) then return 1 else return 0 end";

    /**
     * 释放锁lua脚本
     */
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";


    private static StringRedisTemplate template;

    @Autowired
    public RedisLockUtils(StringRedisTemplate template) {
        RedisLockUtils.template = template;
    }

    /**
     * 获取不阻塞锁 (必须释放锁)
     * @param lockKey 锁的key
     * @param clientId 加锁的客户端Id - 可采用UUID
     * @param expireSecond 失效时间 单位-秒
     *                   PX 是毫秒
     *                   EX 是秒
     * @return
     */
    public static boolean getNoBlockLock(String lockKey, String clientId, int expireSecond) {
        RedisScript<Long> redisScript = RedisScript.of(NO_BLOCK_LOCK_SCRIPT, Long.class);
        Long result = template.execute(redisScript, Collections.singletonList(lockKey), clientId, String.valueOf(expireSecond));
        if(LOCK_SUCCESS.equals(result)){
            return true;
        }
        return false;
    }

    /**
     * 同步锁调用（无需释放）
     * @param lockKey
     * @param clientId
     * @param expireSecond
     * @param call
     * @return
     */
    public static void tryLockWithNotBlock(String lockKey, String clientId, int expireSecond, RedisLockCall call) {
        boolean lock = getNoBlockLock(lockKey, clientId, expireSecond);
        if (lock) {
            try {
                call.call();
            } finally {
                releaseLock(lockKey, clientId);
            }
        }
    }

    /**
     * 同步锁调用（无需释放）
     * @param lockKey
     * @param clientId
     * @param expireSecond
     * @param blockMilliSecond
     * @param sleppMilliSecond
     * @param call
     * @return
     */
    public static void tryLockWithBlock(String lockKey, String clientId, int expireSecond, long blockMilliSecond, long sleppMilliSecond, RedisLockCall call) {
        boolean lock = getBlockLock(lockKey, clientId, expireSecond, blockMilliSecond, sleppMilliSecond);
        if (lock) {
            try {
                call.call();
            }  finally {
                releaseLock(lockKey, clientId);
            }
        }
    }


    /**
     * 获取阻塞锁 (必须释放锁)
     * @param lockKey
     * @param clientId
     * @param expireSecond
     * @param blockMilliSecond 阻塞的毫秒数
     * @Param sleppMilliSecond 每次请求睡眠的毫秒数
     * @return
     */
    public static boolean getBlockLock(String lockKey, String clientId, int expireSecond, long blockMilliSecond, long sleppMilliSecond) {
        while (blockMilliSecond >= 0){
            boolean lock = getNoBlockLock(lockKey, clientId, expireSecond);
            if (lock) {
                return true;
            } else {
                blockMilliSecond -= sleppMilliSecond ;
                try {
                    TimeUnit.MILLISECONDS.sleep(sleppMilliSecond);
                } catch (InterruptedException e) {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 释放锁
     * @param lockKey 锁的key
     * @param clientId 加锁的客户端Id - 可采用UUID
     * @return
     */
    public static boolean releaseLock(String lockKey, String clientId){
        RedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_SCRIPT, Long.class);
        Long result = template.execute(redisScript, Collections.singletonList(lockKey), clientId);
        if(LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }
}
