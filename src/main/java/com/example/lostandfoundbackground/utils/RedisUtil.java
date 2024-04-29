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
     * 将JavaBean转化为Map,然后将其保存到redis中
     * 实体类中的非控制都被转化为String,null值忽略不转化
     *
     * @param bean       要转化的bean
     * @param redisKey   要保存到的redis key
     * @param redisTTL   这个redis key的保存时间
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
        String jsonValue = JsonUtils.javaBeanToJson(bean);
        log.info(jsonValue);
        if (jsonValue != null) {
            stringRedisTemplate.opsForValue().set(redisKey,jsonValue);
        }
        stringRedisTemplate.expire(redisKey, redisTTL, TimeUnit.MINUTES);
    }
}
