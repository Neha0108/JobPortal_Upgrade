package com.recruitment.platform.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

/**
 * Redis configuration for both @Cacheable/@CacheEvict and direct RedisTemplate
 * use (e.g. a future rate-limiter).
 *
 * Uses GenericJacksonJsonRedisSerializer (Jackson 3-based, Spring Data Redis 4.0+)
 * rather than the deprecated Jackson-2-era GenericJackson2JsonRedisSerializer -
 * Boot 4.1 ships Jackson 3 by default, and the old serializer's ObjectMapper type
 * doesn't exist on this classpath at all.
 */
@Configuration
@EnableCaching
public class RedisConfig {

    public static final String CACHE_JOB_LISTINGS = "jobListings";
    public static final String CACHE_JOB_DETAIL = "jobDetail";
    public static final String CACHE_RECRUITER_DASHBOARD = "recruiterDashboard";
    public static final String CACHE_AI_ANALYSIS = "aiAnalysisResults";

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jsonSerializer());
        template.setHashValueSerializer(jsonSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = baseCacheConfig(Duration.ofMinutes(10));

        Map<String, RedisCacheConfiguration> perCacheConfig = Map.of(
                CACHE_JOB_LISTINGS, baseCacheConfig(Duration.ofMinutes(5)),
                CACHE_JOB_DETAIL, baseCacheConfig(Duration.ofMinutes(10)),
                CACHE_RECRUITER_DASHBOARD, baseCacheConfig(Duration.ofMinutes(3)),
                CACHE_AI_ANALYSIS, baseCacheConfig(Duration.ofHours(24))
        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(perCacheConfig)
                .build();
    }

    private RedisCacheConfiguration baseCacheConfig(Duration ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer()));
    }

    private GenericJacksonJsonRedisSerializer jsonSerializer() {
        // Jackson 3's databind bundles java.time (de)serialization support
        // natively - unlike Jackson 2, no separate JavaTimeModule registration
        // is needed here.
        return GenericJacksonJsonRedisSerializer.builder().build();
    }
}