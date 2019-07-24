package com.husen.ci.cache.aspect;

import com.husen.ci.cache.DistributedLockUtils;
import com.husen.ci.cache.annotation.DistributedLock;
import com.husen.ci.framework.api.GlobalCallException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
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

    /**
     * 分布式锁前缀
     */
    private static final String DEFAULT_LOCK_KEY_PRE = "DistributedLock:";

    /**
     * 定义环绕通知
     * @annotation(distributedLock) 切入点
     */
    @Around(value = "@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint point, DistributedLock distributedLock) throws Throwable {
        // 获取注解值
        Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(distributedLock);
        String clientId = StringUtils.isEmpty(distributedLock.clientId()) ? UUID.randomUUID().toString() : distributedLock.clientId();
        String lockKey = StringUtils.isEmpty(annotationAttributes.get("value")) ? point.getSignature().getDeclaringTypeName() + "#" + point.getSignature().getName() : (String)annotationAttributes.get("value");
        int expireSecond = distributedLock.expireSecond();
        boolean isBlock = distributedLock.sleppMilliSecond() != 0 && distributedLock.blockMilliSecond() != 0;
        // 分布式锁Key
        String distributedLockKey =  DEFAULT_LOCK_KEY_PRE + lockKey;
        // 1.获取锁
        boolean isGetLock;
        if (isBlock) {
            // 要阻塞的
            isGetLock = DistributedLockUtils.getBlockLock(distributedLockKey, clientId, expireSecond, distributedLock.blockMilliSecond(), distributedLock.sleppMilliSecond());
        } else {
            // 非阻塞的
            isGetLock = DistributedLockUtils.getNoBlockLock(distributedLockKey, clientId, expireSecond);
        }
        // 锁获取失败 直接抛出异常
        if (!isGetLock) {
            throw new GlobalCallException("内部系统错误,分布式锁获取失败");
        }
        // 2. 执行代码逻辑
        Object result = null;
        try {
            result = point.proceed();
        } finally {
            // 3. 释放锁
            DistributedLockUtils.releaseLock(distributedLockKey, clientId);
        }
        return result;
    }
}
