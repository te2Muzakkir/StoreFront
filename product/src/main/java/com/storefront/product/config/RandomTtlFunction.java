package com.storefront.product.config;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.redis.cache.RedisCacheWriter.TtlFunction;

public class RandomTtlFunction implements TtlFunction {

    private final Duration baseTtl;
    private final Duration jitter;

    public RandomTtlFunction(Duration baseTtl, Duration jitter) {
        this.baseTtl = baseTtl;
        this.jitter = jitter;
    }

    @Override
    public Duration getTimeToLive(Object key, Object value) {
        long baseSeconds = baseTtl.getSeconds();
        long jitterSeconds = jitter.getSeconds();
        long random = ThreadLocalRandom.current().nextLong(-jitterSeconds, jitterSeconds + 1);
        return Duration.ofSeconds(baseSeconds + random);
    }
    
}