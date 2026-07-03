package com.storefront.product.config;

import java.time.Duration;
import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;


@Configuration
public class RedisCacheConfig {
	
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		PolymorphicTypeValidator polymorphicTypeValidator = BasicPolymorphicTypeValidator.builder()
				.allowIfSubType("com.storefront")
				.allowIfSubType("java.math")
		        .allowIfSubType("java.time")
		        .allowIfSubType("java.util")
				.build();
		
		GenericJacksonJsonRedisSerializer serializer = GenericJacksonJsonRedisSerializer.builder()
		                .enableDefaultTyping(polymorphicTypeValidator).build();
		
        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(30))
                        .disableCachingNullValues()
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
        
        return RedisCacheManager.builder(connectionFactory)
        		.transactionAware()
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(Map.of(
                        ProductConstants.PRODUCT_BY_ID, defaultConfig.entryTtl(new RandomTtlFunction(Duration.ofMinutes(30), Duration.ofMinutes(5))),
                        ProductConstants.PRODUCT_BY_CATEGORY, defaultConfig.entryTtl(new RandomTtlFunction(Duration.ofMinutes(60), Duration.ofMinutes(10))),
                        ProductConstants.PRODUCT_LOAD, defaultConfig.entryTtl(new RandomTtlFunction(Duration.ofMinutes(60), Duration.ofMinutes(10))),
                        ProductConstants.PRODUCT_SEARCH, defaultConfig.entryTtl(new RandomTtlFunction(Duration.ofMinutes(10), Duration.ofMinutes(2)))
                )).build();
	}

}