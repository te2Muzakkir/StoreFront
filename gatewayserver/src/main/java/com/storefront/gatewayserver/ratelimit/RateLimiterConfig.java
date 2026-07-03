package com.storefront.gatewayserver.ratelimit;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.storefront.gatewayserver.config.RateLimitProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RateLimiterConfig {

    private final RateLimitProperties properties;

    @Bean("loginRateLimiter")
    public RedisRateLimiter loginRateLimiter() {
        return new RedisRateLimiter(
                properties.getLogin().getReplenishRate(),
                properties.getLogin().getBurstCapacity());
    }
    
    @Bean("refreshRateLimiter")
    public RedisRateLimiter refreshRateLimiter() {
        return new RedisRateLimiter(
                properties.getRefresh().getReplenishRate(),
                properties.getRefresh().getBurstCapacity(),
                properties.getRefresh().getRequestedTokens());
    }

    @Primary
    @Bean("userRateLimiter")
    public RedisRateLimiter userRateLimiter() {
        return new RedisRateLimiter(
                properties.getUser().getReplenishRate(),
                properties.getUser().getBurstCapacity());
    }

}