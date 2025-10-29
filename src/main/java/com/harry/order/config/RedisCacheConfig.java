package com.harry.order.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * 自定义 CacheManager（TTL、序列化、前缀）
 */
@Configuration
public class RedisCacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
            // 能处理 LocalDateTime 的 ObjectMapper
            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // key 用字符串，value 用支持 JavaTime 的 GenericJackson2JsonRedisSerializer
            StringRedisSerializer keySerializer = new StringRedisSerializer();
            GenericJackson2JsonRedisSerializer valueSerializer =
                    new GenericJackson2JsonRedisSerializer(om);

            RedisCacheConfiguration config = RedisCacheConfiguration
                    .defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(5)) // 默认 5 分钟
                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer));

            return RedisCacheManager.builder(factory)
                    .cacheDefaults(config)
                    .build();
    }
}
