package com.storefront.gatewayserver.ratelimit;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Primary
@Component("userKeyResolver")
public class UserKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
    	return exchange.getPrincipal()
    			.cast(Authentication.class)
    			.map(Authentication::getPrincipal)
    			.cast(Jwt.class)
    			.map(jwt -> jwt.getClaimAsString("uid"))
    			.filter(StringUtils::hasText)
    			.defaultIfEmpty("anonymous");
    }

}