package com.example.lostandfoundbackground.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author archi
 */
@Slf4j
@Component
public class RedisUtils {
    private static StringRedisTemplate SPRING_REDIS_TEMPLATE;

    @Autowired
    public void setSpringRedisTemplate(StringRedisTemplate springRedisTemplate){
        RedisUtils.SPRING_REDIS_TEMPLATE = springRedisTemplate;
    }
    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间（秒）
     * @return true / false
     */
    public static boolean expire(String key, long time) {
        try {
            if (time > 0) {
                SPRING_REDIS_TEMPLATE.expire(key, time, TimeUnit.MINUTES);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 根据 key 获取过期时间
     * @param key 键
     * @return
     */
    public static long getExpire(String key) {
        return SPRING_REDIS_TEMPLATE.getExpire(key, TimeUnit.MINUTES);
    }

    /**
     * 判断 key 是否存在
     * @param key 键
     * @return true / false
     */
    public static boolean hasKey(String key) {
        try {
            return SPRING_REDIS_TEMPLATE.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @SuppressWarnings("unchecked") 忽略类型转换警告
     * @param key 键（一个或者多个）
     */
    @SuppressWarnings("unchecked")
    public static void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                SPRING_REDIS_TEMPLATE.delete(key[0]);
            } else {
                //传入一个 Collection<String> 集合
                SPRING_REDIS_TEMPLATE.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public static String get(String key) {
        return StringUtils.hasLength(key) ? SPRING_REDIS_TEMPLATE.opsForValue().get(key) : null;
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true / false
     */
    public static boolean set(String key, String value) {
        try {
            SPRING_REDIS_TEMPLATE.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间（秒），如果 time < 0 则设置无限时间
     * @return true / false
     */
    public static boolean set(String key, String value, long time) {
        try {
            if (time > 0) {
                SPRING_REDIS_TEMPLATE.opsForValue().set(key, value, time, TimeUnit.MINUTES);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 递增大小
     * @return
     */
    public static long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于 0");
        }
        return SPRING_REDIS_TEMPLATE.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 递减大小
     * @return
     */
    public static long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于 0");
        }
        return SPRING_REDIS_TEMPLATE.opsForValue().increment(key, delta);
    }



    /**
     * HashGet
     * @param key 键（no null）
     * @param item 项（no null）
     * @return 值
     */
    public static Object hget(String key, String item) {
        return SPRING_REDIS_TEMPLATE.opsForHash().get(key, item);
    }

    /**
     * 获取 key 对应的 map
     * @param key 键（no null）
     * @return 对应的多个键值
     */
    public static Map<Object, Object> hmget(String key) {
        return SPRING_REDIS_TEMPLATE.opsForHash().entries(key);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 值
     * @return true / false
     */
    public static boolean hmset(String key, Map<Object, Object> map) {
        try {
            SPRING_REDIS_TEMPLATE.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 值
     * @param time 时间
     * @return true / false
     */
    public static boolean hmset(String key, Map<Object, Object> map, long time) {
        try {
            SPRING_REDIS_TEMPLATE.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张 Hash表 中放入数据，如不存在则创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true / false
     */
    public static boolean hset(String key, String item, Object value) {
        try {
            SPRING_REDIS_TEMPLATE.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张 Hash表 中放入数据，并设置时间，如不存在则创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间（如果原来的 Hash表 设置了时间，这里会覆盖）
     * @return true / false
     */
    public static boolean hset(String key, String item, Object value, long time) {
        try {
            SPRING_REDIS_TEMPLATE.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除 Hash表 中的值
     * @param key 键
     * @param item 项（可以多个，no null）
     */
    public static void hdel(String key, Object... item) {
        SPRING_REDIS_TEMPLATE.opsForHash().delete(key, item);
    }

    /**
     * 判断 Hash表 中是否有该键的值
     * @param key 键（no null）
     * @param item 值（no null）
     * @return true / false
     */
    public static boolean hHasKey(String key, String item) {
        return SPRING_REDIS_TEMPLATE.opsForHash().hasKey(key, item);
    }

    /**
     * Hash递增，如果不存在则创建一个，并把新增的值返回
     * @param key 键
     * @param item 项
     * @param by 递增大小 > 0
     * @return
     */
    public static Double hincr(String key, String item, Double by) {
        return SPRING_REDIS_TEMPLATE.opsForHash().increment(key, item, by);
    }

    /**
     * Hash递减
     * @param key 键
     * @param item 项
     * @param by 递减大小
     * @return
     */
    public static Double hdecr(String key, String item, Double by) {
        return SPRING_REDIS_TEMPLATE.opsForHash().increment(key, item, -by);
    }




    /**
     * 将JavaBean转化为Map,然后将其保存到redis中
     * 实体类中的非控制都被转化为String,null值忽略不转化
     *
     * @param bean       要转化的bean
     * @param redisKey   要保存到的redis key
     * @param redisTTL   这个redis key的保存时间
     */
    public static void storeBeanAsHash(Object bean, String redisKey, long redisTTL) {
        Map<String, Object> beanMap = BeanUtil.beanToMap(bean, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> {
                            if (fieldValue == null) {
                                return null; // 如果fieldValue为null，那么从Map中排除这个值
                            }
                            return fieldValue.toString();
                        }));

        SPRING_REDIS_TEMPLATE.opsForHash().putAll(redisKey, beanMap);
        SPRING_REDIS_TEMPLATE.expire(redisKey, redisTTL, TimeUnit.MINUTES);
    }

    public static void storeBeanAsJson(Object bean, String redisKey, long redisTTL) {
        String jsonValue = JsonUtils.javaBeanToJsonString(bean);
        log.info(jsonValue);
        if (jsonValue != null) {
            SPRING_REDIS_TEMPLATE.opsForValue().set(redisKey,jsonValue);
        }
        SPRING_REDIS_TEMPLATE.expire(redisKey, redisTTL, TimeUnit.MINUTES);
    }


}
