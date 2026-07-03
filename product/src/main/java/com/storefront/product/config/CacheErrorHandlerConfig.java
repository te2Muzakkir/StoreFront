package com.storefront.product.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheErrorHandlerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheErrorHandlerConfig.class);

    @Bean
    public CacheErrorHandler cacheErrorHandler() {
    	
        return new CacheErrorHandler() {

            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                LOGGER.error("Redis GET failed. Cache={}, Key={}", cache.getName(), key, exception);
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                LOGGER.error("Redis PUT failed. Cache={}, Key={}", cache.getName(), key, exception);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                LOGGER.error("Redis EVICT failed. Cache={}, Key={}", cache.getName(), key, exception);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception,  Cache cache) {
                LOGGER.error("Redis CLEAR failed. Cache={}", cache.getName(),  exception);
            }
        };
        
    }
    
}