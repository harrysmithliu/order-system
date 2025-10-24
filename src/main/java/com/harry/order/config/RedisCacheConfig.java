package com.harry.order.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisCacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // JSON 序列化，避免 JDK 序列化产生的不可读数据
        GenericJackson2JsonRedisSerializer json = new GenericJackson2JsonRedisSerializer(
                new ObjectMapper()
                        .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        );

        RedisCacheConfiguration defaultConf = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(json))
                .prefixCacheNameWith("order_sys:")
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(10)); // 默认 10 分钟

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultConf)
                // 也可以对不同 cacheName 设置不同 TTL
                .withCacheConfiguration("order:byId",   defaultConf.entryTtl(Duration.ofHours(1)))
                .withCacheConfiguration("user:byId",    defaultConf.entryTtl(Duration.ofHours(1)))
                .withCacheConfiguration("product:byId", defaultConf.entryTtl(Duration.ofHours(1)))
                .withCacheConfiguration("order:pages",  defaultConf.entryTtl(Duration.ofMinutes(3)))
                .build();
    }
}
