package com.storefront.gatewayserver.filter;

import static com.storefront.gatewayserver.config.GatewayConstants.CORRELATION_ID_HEADER;
import static com.storefront.gatewayserver.config.GatewayConstants.CORRELATION_ID_ATTRIBUTE;

import java.util.UUID;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {
	
	 @Override
	    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		 String correlationId = exchange.getRequest().getHeaders().getFirst(CORRELATION_ID_HEADER);
	        if (correlationId == null || correlationId.isBlank())
	            correlationId = UUID.randomUUID().toString();
	        
	        exchange.getAttributes().put(CORRELATION_ID_ATTRIBUTE, correlationId);

	        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
	        		.header(CORRELATION_ID_HEADER, correlationId).build();
	        exchange.getResponse().getHeaders().add(CORRELATION_ID_HEADER, correlationId);
	        return chain.filter(exchange.mutate().request(modifiedRequest).build());
	    }

	    @Override
	    public int getOrder() {
	        return Ordered.HIGHEST_PRECEDENCE;
	    }

}