package com.husen.ci.framework.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.husen.ci.framework.net.bean.HttpResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/***
 @Author:MrHuang
 @Date: 2019/6/11 12:32
 @DESC: TODO 多例的GuavaCache
 @VERSION: 1.0
 ***/
@Slf4j
public class GuavaCache<K,V> {

    /**
     * 由于cahce.getIfPresent
     */
    private Cache<K, Optional<V>> cache;

    /**
     * 默认失效时间
     */
    private static final int DEFAULT_EXPIRE_SECONDS = 60;

    /**
     * 最大存储SIZE
     */
    private static final long MAX_IMUM_SIZE = 5000;

    private GuavaCache() {
       this(DEFAULT_EXPIRE_SECONDS, MAX_IMUM_SIZE);
    }

    private GuavaCache(int seconds, long maxSize) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(seconds, TimeUnit.SECONDS)
                .maximumSize(maxSize)
                .build();
    }

    /**
     * 获取实例
     * 建议通过这种形式获取
     * @return
     */
    public static <K,V> GuavaCache<K,V> getInstace() {
        return new GuavaCache<>();
    }

    /**
     * 获取实例
     * @param seconds 失效时间
     * @param maxSize 最大存储SIZE
     * @return
     */
    public static <K,V> GuavaCache<K,V> getInstace(int seconds, long maxSize) {
        return new GuavaCache<>(seconds, maxSize);
    }

    /**
     * 如果有值返回值，如果没有值返回空
     * @param key
     * @return
     */
    private Optional<V> getLocal(K key) {
        return cache.getIfPresent(key);
    }

    /**
     * 由于Guava的Callable接口中，若采用过期机制，
     * 如果自带的Callable返回了null，get(xx,CallAble)便会抛出异常： CacheLoader returned null for key
     * 故采用Optional + 额外的Supplier
     */
    public V get(K key, Supplier<V> call) {
        Optional<V> local = getLocal(key);
        if (local == null) {
            V v = call.get();
            log.info("GuavaCache Get For Supplier key = {} val = {}", key, v);
            this.reload(key, v);
            return v;
        }
        V v = local.orElse(null);
        log.info("GuavaCache Get For LocalCache key = {} val = {}", key, v);
        return v;
    }

    public void reload(K key, V v) {
       cache.put(key, Optional.ofNullable(v));
    }

    public static void main(String[] args) throws InterruptedException {
        GuavaCache<String,HttpResult> cache = GuavaCache.getInstace();
        HttpResult result = cache.get("123", () -> new HttpResult().setContent("213"));
        result = cache.get("123", () -> new HttpResult().setContent("213"));
        result = cache.get("123", () -> new HttpResult().setContent("213"));
        result = cache.get("123", () -> new HttpResult().setContent("213"));
    }
}
