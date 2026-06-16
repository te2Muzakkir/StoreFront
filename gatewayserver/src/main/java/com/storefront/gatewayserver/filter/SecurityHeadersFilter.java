package com.storefront.gatewayserver.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class SecurityHeadersFilter implements GlobalFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			HttpHeaders headers = exchange.getResponse().getHeaders();
			//Prevent MIME sniffing
			headers.set("X-Content-Type-Options", "nosniff");
			//Prevent click-jacking
			headers.set("X-Frame-Options", "DENY");
			//Restrict referrer information
			headers.set("Referrer-Policy", "strict-origin-when-cross-origin");
			//Restrict browser features
			headers.set("Permissions-Policy", "camera=(), microphone=(), geolocation=()");
			// Basic CSP
			headers.set("Content-Security-Policy",
					"default-src 'self'; frame-ancestors 'none'; object-src 'none';");
			//Remove identifying headers
			headers.remove("Server");
			headers.remove("X-Powered-By");
		}));
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
	
}