package com.husen.ci.cache.aspect;

import com.husen.ci.cache.DistributedLimitUtils;
import com.husen.ci.cache.annotation.DistributedLimit;
import com.husen.ci.framework.api.GlobalCallException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/***
 @Author:MrHuang
 @Date: 2019/7/24 17:17
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
@Aspect
@Component
public class DistributedLimitAspect {

    /**
     * 限流Key前缀
     */
    private static final String DEFAULT_LIMIT_KEY_PRE = "DistributedLimit:";

    /**
     * 定义环绕通知
     * @annotation(distributedLock) 切入点
     */
    @Before("@annotation(distributedLimit)")
    public void before(JoinPoint point, DistributedLimit distributedLimit){
        // 获取注解值
        Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(distributedLimit);
        String limitKey = StringUtils.isEmpty(annotationAttributes.get("value")) ? point.getSignature().getDeclaringTypeName() + "#" + point.getSignature().getName() : (String)annotationAttributes.get("value");
        int limitNum = distributedLimit.limitNum();
        int expireSecond = distributedLimit.expireSecond();
        String distributedLimitKey = DEFAULT_LIMIT_KEY_PRE + limitKey;
        boolean isLimit = DistributedLimitUtils.isLimit(distributedLimitKey, limitNum, expireSecond);
        log.info("DistributedLimitAspect isLimit = {}", isLimit);
        if (isLimit) {
            throw new GlobalCallException("接口限流");
        }
    }
}
