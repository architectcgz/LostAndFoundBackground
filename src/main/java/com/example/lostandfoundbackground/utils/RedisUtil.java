package com.example.lostandfoundbackground.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisUtil {

    /**
     * Converts a Java Bean object to a Map and stores it in Redis.
     * The field values are converted to strings, and null values are excluded.
     *
     * @param bean       The Java Bean object to convert.
     * @param redisKey   The Redis key where the Map will be stored.
     * @param redisTTL   The time-to-live (TTL) for the Redis key (in minutes).
     */
    public static void storeBeanAsHash(StringRedisTemplate stringRedisTemplate, Object bean, String redisKey, long redisTTL) {

        Map<String, Object> beanMap = BeanUtil.beanToMap(bean, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> {
                            if (fieldValue == null) {
                                return null; // Exclude the field from the map if value is null
                            }
                            return fieldValue.toString();
                        }));

        stringRedisTemplate.opsForHash().putAll(redisKey, beanMap);
        stringRedisTemplate.expire(redisKey, redisTTL, TimeUnit.MINUTES);
    }

    public static void storeBeanAsJson(StringRedisTemplate stringRedisTemplate, Object bean, String redisKey, long redisTTL) {
        String jsonValue = JsonUtils.objectToJson(bean);
        log.info(jsonValue);
        if (jsonValue != null) {
            stringRedisTemplate.opsForValue().set(redisKey,jsonValue);
        }
        stringRedisTemplate.expire(redisKey, redisTTL, TimeUnit.MINUTES);
    }
}
