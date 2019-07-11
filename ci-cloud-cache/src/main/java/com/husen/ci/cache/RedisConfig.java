package com.husen.ci.cache;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Author: huangxincheng
 * <p>
 * <p>
 **/
@Configuration
public class RedisConfig {

    /**
     * 选择redis作为默认缓存工具
     *
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheManager rcm = RedisCacheManager.create(factory);
        return rcm;
    }

    /**
     * retemplate相关配置
     *
     * @param factory
     * @return
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//        // 值采用json序列化
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new StringRedisSerializer());
//        // 设置hash key 和value序列化模式
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(new StringRedisSerializer());
//        template.setDefaultSerializer(new StringRedisSerializer());
//        template.setConnectionFactory(factory);
//        template.afterPropertiesSet();
//        return template;
        return new StringRedisTemplate(factory);
    }
}