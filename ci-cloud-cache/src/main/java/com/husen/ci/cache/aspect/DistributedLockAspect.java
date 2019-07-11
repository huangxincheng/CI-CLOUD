package com.husen.ci.cache.aspect;

import com.husen.ci.cache.RedisLockUtils;
import com.husen.ci.cache.annotation.DistributedLock;
import com.husen.ci.framework.api.GlobalApiException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

/***
 @Author:MrHuang
 @Date: 2019/7/11 11:12
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
@Aspect
@Component
public class DistributedLockAspect {

    private static final String DEFAULT_LOCK_KEY_PRE = "DistributedLock:";

    /**
     * 定义环绕通知
     */
    @Around(value = "@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint point, DistributedLock distributedLock) throws Throwable {
        String clientId = StringUtils.isEmpty(distributedLock.clientId()) ? UUID.randomUUID().toString() : distributedLock.clientId();
        String lockKey = StringUtils.isEmpty(distributedLock.lockKey()) ? DEFAULT_LOCK_KEY_PRE + clientId : distributedLock.lockKey();
        int expireSecond = distributedLock.expireSecond();
        boolean isBlock = distributedLock.sleppMilliSecond() != 0 && distributedLock.blockMilliSecond() != 0;
        boolean isGetLock = false;
        if (isBlock) {
            // 要阻塞的
            isGetLock = RedisLockUtils.getBlockLock(lockKey, clientId, expireSecond, distributedLock.blockMilliSecond(), distributedLock.sleppMilliSecond());
        } else {
            // 非阻塞的
            isGetLock = RedisLockUtils.getNoBlockLock(lockKey, clientId, expireSecond);
        }
        // 锁获取失败 直接抛出异常
        if (!isGetLock) {
            throw new GlobalApiException("内部系统错误,锁获取失败");
        }
        Object result = null;
        try {
            result = point.proceed();
        } finally {
            RedisLockUtils.releaseLock(lockKey, clientId);
        }
        return result;
    }
}