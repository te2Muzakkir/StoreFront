package com.storefront.gatewayserver.filter;

import java.util.Set;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class ContentTypeValidationFilter implements GlobalFilter, Ordered {

	private static final Set<MediaType> ALLOWED_TYPES =
			Set.of(
					MediaType.APPLICATION_JSON,
					MediaType.MULTIPART_FORM_DATA
					);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		HttpMethod method = exchange.getRequest().getMethod();
		if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
			MediaType contentType = exchange.getRequest().getHeaders().getContentType();
			if (contentType != null && ALLOWED_TYPES.stream().noneMatch(type -> type.isCompatibleWith(contentType))) {
				exchange.getResponse().setStatusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
				return exchange.getResponse().setComplete();
			}
		}
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 20;
	}

}