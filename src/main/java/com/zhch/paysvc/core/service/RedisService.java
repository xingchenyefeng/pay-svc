package com.zhch.paysvc.core.service;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;


/**
 * @author lumos
 */
@ConditionalOnClass(StringRedisTemplate.class)
@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    public List<Object> batchOps(final List<Consumer<StringRedisConnection>> cs) {
        List<Object> values = null;
        if (CollectionUtil.isNotEmpty(cs)) {
            values = stringRedisTemplate.executePipelined((RedisCallback<Object>) redisConnection -> {
                StringRedisConnection stringRedisConn = (StringRedisConnection) redisConnection;
                for (Consumer<StringRedisConnection> c : cs) {
                    c.accept(stringRedisConn);
                }
                return null;
            });
        }
        return values;
    }
}