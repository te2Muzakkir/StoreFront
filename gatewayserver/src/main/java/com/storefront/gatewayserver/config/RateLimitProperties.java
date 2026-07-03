package com.storefront.gatewayserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "storefront.rate-limit")
@Getter
@Setter
public class RateLimitProperties {

    private Limit login = new Limit();
    private Limit user = new Limit();
    private RefreshLimit refresh = new RefreshLimit();

    @Getter
    @Setter
    public static class Limit {
        private int replenishRate;
        private int burstCapacity;
    }
    
    @Getter
    @Setter
    public static class RefreshLimit extends Limit {
        private int requestedTokens;
    }

}