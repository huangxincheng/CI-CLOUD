package com.husen.ci.cache;

import com.husen.ci.framework.api.GlobalCallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

/***
 @Author:MrHuang
 @Date: 2019/7/24 17:02
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
@Component
public class DistributedLimitUtils {

    /**
     * 限流。
     * 如果已限流返回：0
     */
    private static final Long LIMIT_FAIL = 0L;
    /**
     * 限流的lua脚本
     */
    private static final String LIMIT_SCRIPT =  "local key = KEYS[1] \n" +
                                                "local limit = tonumber(ARGV[1])\n" +
                                                "local curentLimit = tonumber(redis.call('get', key) or '0')\n" +
                                                "if curentLimit + 1 > limit then\n" +
                                                "    return 0;\n" +
                                                "else\n" +
                                                "    redis.call('INCRBY', key, 1)\n" +
                                                "    redis.call('EXPIRE', key, ARGV[2])\n" +
                                                "    return curentLimit + 1\n" +
                                                "end";


    private static StringRedisTemplate template;

    @Autowired
    public DistributedLimitUtils(StringRedisTemplate template) {
        DistributedLimitUtils.template = template;
    }

    /**
     * 是否限流
     * @return
     *      true  已达到限流标准
     *      false 未达到限流标准
     */
    public static boolean isLimit(String limitKey, long limitNum, long expireSecond) {
        RedisScript<Long> redisScript = RedisScript.of(LIMIT_SCRIPT, Long.class);
        Long result = template.execute(redisScript, Collections.singletonList(limitKey), String.valueOf(limitNum), String.valueOf(expireSecond));
        return LIMIT_FAIL.equals(result);
    }

    /**
     * 限流方法
     * @param limitKey
     * @param limitNum
     * @param expireSecond
     * @param call
     * @return
     */
    public static void limit(String limitKey, long limitNum, long expireSecond, DistributedCall call) {
        if (isLimit(limitKey, limitNum, expireSecond)) {
            call.call();
        } else {
            throw new GlobalCallException("接口限流");
        }
    }
}
