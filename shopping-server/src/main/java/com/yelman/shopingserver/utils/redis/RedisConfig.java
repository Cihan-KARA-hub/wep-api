package com.yelman.shopingserver.utils.redis;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    // Cache Manager Bean
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // RedisCacheConfiguration ile cache yapılandırması
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues() // Null değerlerin cache'e eklenmesini engeller
                .serializeKeysWith(RedisCacheConfiguration.defaultCacheConfig().getKeySerializationPair( )) // Anahtarları String olarak serileştir
                .serializeValuesWith(RedisCacheConfiguration.defaultCacheConfig().getValueSerializationPair( )); // Değerleri JSON formatında serileştir

        // RedisCacheManager ile cache manager oluşturuluyor
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfig) // Varsayılan cache yapılandırmasını kullan
                .build();
    }

    // RedisTemplate Bean
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory); // RedisConnectionFactory bağlantısını ayarla

        // Anahtar ve değer serileştiricilerini ayarla
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // Anahtarları String olarak serileştir
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // Değerleri JSON formatında serileştir

        return redisTemplate; // RedisTemplate objesini döndür
    }
}
