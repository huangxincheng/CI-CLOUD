package com.husen.ci.framework.aspect;

import com.alibaba.fastjson.JSONObject;
import com.husen.ci.framework.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/***
 @Author:MrHuang
 @Date: 2019/7/8 18:29
 @DESC: TODO 打印方法耗时-参数-返回值的注解的切面
 @VERSION: 1.0
 ***/
@Slf4j
@Aspect
@Component
public class PrintMethodAspect {


    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.husen.ci.framework.annotation.PrintMethod)")
    public void printMethodAnnotationPointcut() {
    }

    /**
     * 定义环绕通知
     */
    @Around(value = "printMethodAnnotationPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beforeTimeMillis = System.currentTimeMillis();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        JSONObject argsObj = this.getArgsNameValue(methodSignature.getParameterNames(), point.getArgs());
        Object result = point.proceed();
        log.info("method = {} params = {} returns = {} execTime = {}ms.",
                method.getDeclaringClass().getName() + "#" + method.getName(),
                argsObj, JSONUtils.object2Json(result),
                System.currentTimeMillis() - beforeTimeMillis);
        return result;
    }

    /**
     * 获取参数名和值
     * @param names names
     * @param values values
     * @return
     */
    private JSONObject getArgsNameValue(String[] names, Object[] values) {
        JSONObject obj = new JSONObject();
        if (names != null && names.length > 0) {
            for (int i = 0; i < names.length; i++) {
                obj.put(names[i], values[i]);
            }
        }
        return obj;
    }
}
