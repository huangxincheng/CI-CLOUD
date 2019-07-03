package com.husen.ci.cache;

import com.husen.ci.framework.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/***
 @Author:MrHuang
 @Date: 2019/6/21 15:05
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
@Component
public class RedisUtils {

    private static StringRedisTemplate template;

    private static ValueOperations<String, String> vos;

    private static HashOperations<String, String, String> hos;

    private static ListOperations<String, String> los;

    private static SetOperations<String, String> sos;

    private static ZSetOperations<String, String> zsos;

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
    /**
     * 限流 如果已限流返回：0
     */
    private static final Long LIMIT_FAIL = 0L;
    /**
     * 限流 lua脚本
     */
    private static final String LIMIT_SCRIPT =
                    "local key = KEYS[1] \n" +
                    "local limit = tonumber(ARGV[1])\n" +
                    "local curentLimit = tonumber(redis.call('get', key) or '0')\n" +
                    "if curentLimit + 1 > limit then\n" +
                    "    return 0;\n" +
                    "else\n" +
                    "    redis.call('INCRBY', key, 1)\n" +
                    "    redis.call('EXPIRE', key, ARGV[2])\n" +
                    "    return curentLimit + 1\n" +
                    "end";


    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate template) {
        RedisUtils.template = template;
        RedisUtils.vos = template.opsForValue();
        RedisUtils.hos = template.opsForHash();
        RedisUtils.los = template.opsForList();
        RedisUtils.sos = template.opsForSet();
        RedisUtils.zsos = template.opsForZSet();
    }


    /***--------------------------- Base ------------------------------- ***/

    /**
     * 删除某个key
     * @param key
     * @return
     */
    public static Boolean delete(String key) {
        try {
            return template.delete(key);
        } catch (Exception e) {
            log.error("RedisUtils del", e);
            return null;
        }
    }

    /**
     * 删除一些key
     * @param keys
     * @return
     */
    public static Long delete(String ... keys) {
        return delete(Arrays.asList(keys));
    }

    /**
     * 删除一些key
     * @param keys
     * @return
     */
    public static Long delete(Collection<String> keys) {
        try {
            return template.delete(keys);
        } catch (Exception e) {
            log.error("RedisUtils delKeys");
            return null;
        }
    }

    /**
     * 设置失效时间 单位秒
     * @param key
     * @param timeoutSecond
     * @return
     */
    public static Boolean expire(String key, long timeoutSecond) {
        try {
            return template.expire(key, timeoutSecond, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("RedisUtils expire", e);
            return null;
        }
    }

    /**
     * 设置失效时间 具体到日期
     * @param key
     * @param date
     * @return
     */
    public static Boolean expireAt(String key, Date date) {
        try {
            return template.expireAt(key, date);
        } catch (Exception e) {
            log.error("RedisUtils expireAt", e);
            return null;
        }
    }

    /**
     * 获取Key的类型
     * @param key
     * @return
     */
    public static DataType type(String key) {
        try {
            DataType type = template.type(key);
            return type;
        } catch (Exception e) {
            log.error("RedisUtils type", e);
            return null;
        }
    }

    /**
     * 判断是否key存在
     * @param key
     * @return
     */
    public static Boolean hasKey(String key) {
        try {
            return template.hasKey(key);
        } catch (Exception e) {
            log.error("RedisUtils exists", e);
            return null;
        }
    }

    /**
     * 获取失效时间
     * @param key
     * @return
     */
    public static Long getExpire(String key) {
        try {
            return template.getExpire(key);
        } catch (Exception e) {
            log.error("RedisUtils getExpire", e);
            return null;
        }
    }


    /**
     * 获取不阻塞锁
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
     * 获取阻塞锁
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


    /**
     * 是否限流
     * @return
     *      true  已达到限流标准
     *      false 未达到限流标准
     */
    public static boolean isLimit(String limitKey, long limitNum, long expireSecond) {
        RedisScript<Long> redisScript = RedisScript.of(LIMIT_SCRIPT, Long.class);
        Long result = template.execute(redisScript, Collections.singletonList(limitKey), String.valueOf(limitNum), String.valueOf(expireSecond));
        if(LIMIT_FAIL.equals(result)){
            return true;
        }
        return false;
    }







    /***--------------------------- String ------------------------------- ***/

    /**
     * Redis Get
     * @param key
     * @return
     */
    public static String getForString(String key) {
        try {
            return vos.get(key);
        } catch (Exception e) {
            log.error("RedisUtils get", e);
            return null;
        }
    }

    public static <T> T getObjectForString(String key, Class<T> clazz) {
        return JSONUtils.json2Bean(getForString(key), clazz);
    }

    public static <T> List<T> getArrayForString(String key, Class<T> clazz) {
        return JSONUtils.json2List(getForString(key), clazz);
    }


    public static boolean setForString(String key, String value) {
        try {
            vos.set(key, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils set", e);
            return false;
        }
    }

    public static boolean setForString(String key, Object value) {
        return setForString(key, JSONUtils.object2Json(value));
    }

    public static boolean setForString(String key, Object value, int expire) {
        return setForString(key, JSONUtils.object2Json(value), expire);
    }

    public static boolean setForString(String key, String value, int expire) {
        try {
            vos.set(key, value, expire, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils set", e);
            return false;
        }
    }

    public static boolean setIfAbsentForString(String key, String value) {
        try {
            return Optional.ofNullable(vos.setIfAbsent(key, value)).orElse(false);
        } catch (Exception e) {
            log.error("RedisUtils setnx", e);
            return false;
        }
    }

    public static Long incrementForString(String key, long delta) {
        try {
            return vos.increment(key, delta);
        } catch (Exception e) {
            log.error("RedisUtils incr", e);
            return null;
        }
    }


    public static Double incrementForString(String key, double delta) {
        try {
            return vos.increment(key, delta);
        } catch (Exception e) {
            log.error("RedisUtil incr", e);
            return null;
        }
    }

    public static Long decrementForString(String key, long delta) {
        return incrementForString(key, -delta);
    }


    public static Double decrementForString(String key, double delta) {
        return incrementForString(key, -delta);
    }


    public static List<String> multiGetForString(String... keys) {
        return multiGetForString(Arrays.asList(keys));
    }

    public static List<String> multiGetForString(Collection<String> keys) {
        try {
            return vos.multiGet(keys);
        } catch (Exception e) {
            log.error("RedisUtils mget", e);
            return null;
        }
    }

    public static boolean multiSetForString(Map<String, String> map) {
        try {
            vos.multiSet(map);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils mset", e);
            return false;
        }
    }

    public static String getAndSetForString(String key, String value) {
        try {
            return vos.getAndSet(key, value);
        } catch (Exception e) {
            log.error("RedisUtils getAndSet", e);
            return null;
        }
    }


    public static Long sizeForString(String key) {
        try {
            return vos.size(key);
        } catch (Exception e) {
            log.error("RedisUtils size", e);
            return null;
        }
    }

    public static Boolean multiSetIfAbsentForString(Map<String,String> map) {
        try {
            return vos.multiSetIfAbsent(map);
        } catch (Exception e) {
            log.error("RedisUtil msetnx", e);
            return null;
        }
    }


    /***--------------------------- Hash ------------------------------- ***/

    public static Long deleteForHash(String key, String ...hashKeys) {
        try {
            return hos.delete(key, hashKeys);
        } catch (Exception e) {
            log.error("RedisUtils deleteForHash", e);
            return null;
        }
    }

    public Boolean hasKeyForHash(String key, String hashKey) {
        try {
            return hos.hasKey(key, hashKey);
        } catch (Exception e) {
            log.error("RedisUtils hasKeyForHash", e);
            return null;
        }
    }

    public static String getOneForHash(String key, String hashKey) {
        try {
            return hos.get(key, hashKey);
        } catch (Exception e) {
            log.error("RedisUtils getOneForHash", e);
            return null;
        }
    }

    public static List<String> getManyForHash(String key, Collection<String> hashKeys) {
        try {
            return hos.multiGet(key, hashKeys);
        } catch (Exception e) {
            log.error("RedisUtils getManyForHash");
            return null;
        }
    }

    public static List<String> getManyForHash(String key, String ... hashKeys) {
        return getManyForHash(key, Arrays.asList(hashKeys));
    }

    public static Long incrForHash(String key, String hashKey, long delta) {
        try {
            return hos.increment(key, hashKey, delta);
        } catch (Exception e) {
            log.error("RedisUtils incrForHash");
            return null;
        }
    }

    public static Double incrForHash(String key, String hashKey, double delta) {
        try {
            return hos.increment(key, hashKey, delta);
        } catch (Exception e) {
            log.error("RedisUtils incrForHash");
            return null;
        }
    }

    public static Long getSizeForHash(String key) {
        try {
            return hos.size(key);
        } catch (Exception e) {
            log.error("RedisUtils getSizeForHash");
            return null;
        }
    }

    public static boolean putManyForHash(String key, Map<String, String> map) {
        try {
            hos.putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils putManyForHash");
            return false;
        }
    }

    public static boolean putOneForHash(String key, String hashKey, String value) {
        try {
            hos.put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils putOneForHash", e);
            return false;
        }
    }


    public static List<String> getHashKeyForHash(String key) {
        try {
            return hos.values(key);
        } catch (Exception e) {
            log.error("RedisUtils getHashKeyForHash", e);
            return null;
        }
    }

    public static Map<String, String> getHashKeyValueForHash(String key) {
        try {
            return hos.entries(key);
        } catch (Exception e) {
            log.error("RedisUtils getHashKeyValueForHash", e);
            return null;
        }
    }

    public static List<Map.Entry<String, String>> getEntryForHash(String key) {
        try {
            List<Map.Entry<String, String>> entryList = new ArrayList<>();
            Cursor<Map.Entry<String, String>> curson = hos.scan(key, ScanOptions.NONE);
            while (curson.hasNext()) {
                Map.Entry<String, String> entry = curson.next();
                entryList.add(entry);
            }
            return entryList;
        } catch (Exception e) {
            return  null;
        }
    }


    /***--------------------------- List ------------------------------- ***/

    /**
     * 往左put值
     * @param key
     * @param value
     * @return
     */
    public static Long leftPushForList(String key, String value) {
        try {
            return los.leftPush(key, value);
        } catch (Exception e) {
            log.error("RedisUtils leftPushForList", e);
            return null;
        }
    }

    /**
     * 将value值放在pivot值得左边
     * @param key key
     * @param pivot pivot
     * @param value value
     * @return
     */
    public static Long leftPushForList(String key, String pivot, String value) {
        try {
            return los.leftPush(key, pivot, value);
        } catch (Exception e) {
            log.error("RedisUtils leftPushForList", e);
            return null;
        }
    }


    public static String leftPopForList(String key) {
        try {
            return los.leftPop(key);
        } catch (Exception e) {
            log.error("RedisUtils leftPopForList", e);
            return null;
        }
    }

    public static String leftPopForList(String key, long timeout) {
        try {
            return los.leftPop(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("RedisUtils leftPopForList", e);
            return null;
        }
    }

    public static Long leftPushAllForList(String key, String ... values) {
        try {
            return los.leftPushAll(key, values);
        } catch (Exception e) {
            log.error("RedisUtils leftPushAllForList", e);
            return null;
        }
    }

    public static Long leftPushAllForLisr(String key, Collection<String> values) {
        try {
            return los.leftPushAll(key, values);
        } catch (Exception e) {
            log.error("RedisUtils leftPushAllForLisr", e);
            return null;
        }
    }


    public static Long leftPushIfPresentForList(String key, String value) {
        try {
            return los.leftPushIfPresent(key, value);
        } catch (Exception e) {
            log.error("RedisUtils leftPushIfPresentForList", e);
            return null;
        }
    }


    public static Long rightPushForList(String key, String value) {
        try {
            return los.rightPush(key, value);
        } catch (Exception e) {
            log.error("RedisUtils rightPushForList", e);
            return null;
        }
    }


    /**
     * 将value值放在pivot值得右边
     * @param key key
     * @param pivot pivot
     * @param value value
     * @return
     */
    public static Long rightPushForList(String key, String pivot, String value ) {
        try {
            return los.rightPush(key, pivot, value);
        } catch (Exception e) {
            log.error("RedisUtils rightPushForList", e);
            return null;
        }
    }


    public static String rightPopForList(String key) {
        try {
            return los.rightPop(key);
        } catch (Exception e) {
            log.error("RedisUtils rightPopForList", e);
            return null;
        }
    }

    public static String rightPopForList(String key, long timeout) {
        try {
            return los.rightPop(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("RedisUtils rightPopForList", e);
            return null;
        }
    }


    public static Long rightPushAllForList(String key, String ... values) {
        try {
            return los.rightPushAll(key, values);
        } catch (Exception e) {
            log.error("RedisUtils rightPushAllForList", e);
            return null;
        }
    }

    public static Long rightPushAllForList(String key, Collection<String> values) {
        try {
            return los.rightPushAll(key, values);
        } catch (Exception e) {
            log.error("RedisUtils rightPushAllForList", e);
            return null;
        }
    }

    public static Long rightPushIfPresentForList(String key, String value) {
        try {
            return los.rightPushIfPresent(key, value);
        } catch (Exception e) {
            log.error("RedisUtils rightPushIfPresentForList", e);
            return null;
        }
    }


    public static List<String> lrangeForList(String key, long start, long end) {
        try {
            return los.range(key, start, end);
        } catch (Exception e) {
            log.error("RedisUtils lrangeForList", e);
            return null;
        }
    }

    /**
     * lrem [lrem key count value] :移除等于value的元素，
     * 当count>0时，从表头开始查找，移除count个 移除等于value的元素；
     * 当count=0时，从表头开始查找，移除所有等于value的；
     * 当count<0时，从表尾开始查找，移除count个 移除等于value的元素。
     * @param key key
     * @param count count
     * @param value value
     * @return
     */
    public static Long removeForList(String key, long count, String value) {
        try {
            return los.remove(key, count, value);
        } catch (Exception e) {
            log.error("RedisUtils removeForList", e);
            return null;
        }
    }


    /**
     * 根据下表获取列表中的值，下标是从0开始的
     * @param key key
     * @param index index
     * @return
     */
    public static String indexForList(String key, long index) {
        try {
            return los.index(key, index);
        } catch (Exception e) {
            log.error("RedisUtils indexForList", e);
            return null;
        }
    }

    /**
     * 将value的值设置到列表index位置
     * 数组不能越界
     * @param key key
     * @param index index
     * @param value value
     * @return
     */
    public static boolean setForList(String key, long index, String value) {
        try {
            los.set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils setForList", e);
            return false;
        }
    }

    /**
     * 修剪现有列表，使其只保留指定的指定范围的元素，起始和停止都是基于0的索引
     * @param key key
     * @param start start
     * @param end end
     * @return
     */
    public static boolean trimForList(String key, long start, long end) {
        try {
            los.trim(key, start, end);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils trimForList", e);
            return false;
        }
    }


    /***--------------------------- Set ------------------------------- ***/

    /**
     * SADD key member1 [member2]
     * 向集合添加一个或多个成员
     * @param key
     * @param values
     * @return
     */
    public static Long addForSet(String key, String ...values) {
        try {
            return sos.add(key, values);
        } catch (Exception e) {
            log.error("RedisUtils addForSet", e);
            return null;
        }
    }

    /**
     * SREM key member1 [member2]
     * 移除集合中一个或多个成员
     * @param key
     * @param values
     * @return
     */
    public static Long removeForSet(String key, String ... values) {
        try {
            return sos.remove(key, (Object[]) values);
        } catch (Exception e) {
            log.error("RedisUtils removeForSet", e);
            return null;
        }
    }

    /**
     * SPOP key
     * 移除并返回集合中的一个随机元素
     * @param key
     * @return
     */
    public static String popForSet(String key) {
        try {
            return sos.pop(key);
        } catch (Exception e) {
            log.error("RedisUtils popForSet", e);
            return null;
        }
    }

    /**
     * SPOP key
     * 移除并返回集合中的多个随机元素
     * @param key
     * @return
     */
    public static List<String> popForSet(String key, long count) {
        try {
            return sos.pop(key, count);
        } catch (Exception e) {
            log.error("RedisUtils popForSet", e);
            return null;
        }
    }

    /**
     * 	SMOVE source destination member
     * 将 member 元素从 source 集合移动到 destination 集合
     * @param key
     * @param value
     * @param destKey
     * @return
     */
    public static Boolean moveForSet(String key, String value, String destKey) {
        try {
            return sos.move(key, value, destKey);
        } catch (Exception e) {
            log.error("RedisUtils moveForSet", e);
            return null;
        }
    }

    /**
     * SCARD key
     * 获取集合的成员数
     * @param key
     * @return
     */
    public static Long sizeForSet(String key) {
        try {
            return sos.size(key);
        } catch (Exception e) {
            log.error("RedisUtils sizeForSet", e);
            return null;
        }
    }

    /**
     * SISMEMBER key member
     * 判断 member 元素是否是集合 key 的成员
     * @param key
     * @param o
     * @return
     */
    public static Boolean isMemberForSet(String key, Object o) {
        try {
            return sos.isMember(key, o);
        } catch (Exception e) {
            log.error("RedisUtils isMemberForSet", e);
            return null;
        }
    }

    /**
     * SDIFF key1 [key2]
     * 返回给定所有集合的差集
     * @param key
     * @param otherKey
     * @return
     */
    public static Set<String> differenceForSet(String key, String otherKey) {
        try {
            return sos.difference(key, otherKey);
        } catch (Exception e) {
            log.error("RedisUtils differenceForSet", e);
            return null;
        }
    }



    /***--------------------------- ZSet ------------------------------- ***/


    /**
     * 	ZADD key score1 member1 [score2 member2]
     * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
     * @param key
     * @param value
     * @param score
     * @return
     */
    public static boolean addForZSet(String key, String value, double score) {
        try {
            return Optional.ofNullable(zsos.add(key, value, score)).orElse(false);
        } catch (Exception e) {
            log.error("RedisUtils addForZSet", e);
        }
        return false;
    }

    /**
     * 	ZADD key score1 member1 [score2 member2]
     * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
     * @param key
     * @param tuples
     * @return
     */
    public static Long addForZSet(String key, Set<ZSetOperations.TypedTuple<String>> tuples) {
        return zsos.add(key, tuples);
    }

    /**
     * ZCARD key
     * 获取有序集合的成员数
     * @param key
     * @return
     */
    public static Long sizeForZSet(String key) {
        try {
            return zsos.size(key);
        } catch (Exception e) {
            log.error("RedisUtils sizeForZSet", e);
        }
        return null;
    }

    /**
     * ZCOUNT key min max
     * 计算在有序集合中指定区间分数的成员数
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Long countForZSet(String key, double min, double max) {
        return zsos.count(key, min, max);
    }

    /**
     * ZINCRBY key increment member
     * 有序集合中对指定成员的分数加上增量 increment
     * @param key
     * @param value
     * @param delta
     * @return
     */
    public static Double incrementScoreForZSet(String key, String value, double delta) {
        return zsos.incrementScore(key, value, delta);
    }

    /**
     * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     * 通过分数返回有序集合指定区间内的成员
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<String> rangeByScoreForZSet(String key, double min, double max) {
        return zsos.rangeByScore(key, min, max);
    }

    /**
     * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     * 通过分数返回有序集合指定区间内的成员
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public static Set<String> rangeByScoreForZSet(String key, double min, double max, long offset, long count) {
        return zsos.rangeByScore(key, min, max, offset, count);
    }

    /**
     * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     * 通过分数返回有序集合指定区间内的成员
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<String>> rangeByScoreWithScoresForZSet(String key, double min, double max) {
        return zsos.rangeByScoreWithScores(key, min, max);
    }

    /**
     * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     * 通过分数返回有序集合指定区间内的成员
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<String>> rangeByScoreWithScoresForZSet(String key, double min, double max, long offset, long count) {
        return zsos.rangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * ZRANK key member
     * 返回有序集合中指定成员的索引
     * @param key
     * @param value
     * @return
     */
    public static Long rankForZSet(String key, Object value) {
        return zsos.rank(key, value);
    }

    /**
     * ZREM key member [member ...]
     * 移除有序集合中的一个或多个成员
     * @param key
     * @param values
     * @return
     */
    public static Long removeForZSet(String key, Object ... values) {
        return zsos.remove(key, values);
    }

    /**
     * 	ZREMRANGEBYLEX key min max
     * 移除有序集合中给定的字典区间的所有成员
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Long removeRangeForZSet(String key, long start, long end) {
       return zsos.removeRange(key, start, end);
    }

    /**
     * ZREMRANGEBYSCORE key min max
     * 移除有序集合中给定的分数区间的所有成员
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Long removeRangeByScoreForZSet(String key, double min, double max) {
        return zsos.removeRangeByScore(key, min, max);
    }

    /**
     * ZREVRANGE key start stop [WITHSCORES]
     * 返回有序集中指定区间内的成员，通过索引，分数从高到底
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<String> reverseRangeForZSet(String key, long start, long end) {
       return zsos.reverseRange(key, start, end);
    }

    /**
     * ZREVRANGE key start stop [WITHSCORES]
     * 返回有序集中指定区间内的成员，通过索引，分数从高到底
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScoresForZSet(String key, long start, long end) {
        return zsos.reverseRangeWithScores(key, start, end);
    }

    /**
     * 	ZREVRANGEBYSCORE key max min [WITHSCORES]
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<String> reverseRangeByScoreForZSet(String key, double min, double max) {
        return zsos.reverseRangeByScore(key, min, max);
    }

    /**
     * 	ZREVRANGEBYSCORE key max min [WITHSCORES]
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<String> reverseRangeByScoreForZSet(String key, double min, double max, long offset, long count) {
        return zsos.reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * 	ZREVRANGEBYSCORE key max min [WITHSCORES]
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<String>> reverseRangeByScoreWithScoresForZSet(String key, double min, double max) {
        return zsos.reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * 	ZREVRANGEBYSCORE key max min [WITHSCORES]
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<String>> reverseRangeByScoreWithScoresForZSet(String key, double min, double max, long offset, long count) {
        return zsos.reverseRangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * ZREVRANK key member
     * 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序
     * @param key
     * @param value
     * @return
     */
    public static Long reverseRankForZSet(String key, Object value) {
        return zsos.reverseRank(key, value);
    }

    /**
     * 	ZSCORE key member
     * 返回有序集中，成员的分数值
     * @param key
     * @param value
     * @return
     */
    public static Double scoreForZSet(String key, Object value) {
        return zsos.score(key, value);
    }

    /**
     * 	ZSCAN key cursor [MATCH pattern] [COUNT count]
     * 迭代有序集合中的元素（包括元素成员和元素分值）
     * @param key
     * @param options
     * @return
     */
    public static Cursor<ZSetOperations.TypedTuple<String>> scanForZSet(String key, ScanOptions options) {
        return zsos.scan(key, options);
    }
}
