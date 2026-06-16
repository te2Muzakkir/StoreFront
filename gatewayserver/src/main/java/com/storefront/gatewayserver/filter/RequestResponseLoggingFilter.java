package com.storefront.gatewayserver.filter;

import static com.storefront.gatewayserver.config.GatewayConstants.CORRELATION_ID_ATTRIBUTE;
import static com.storefront.gatewayserver.config.GatewayConstants.REQUEST_START_TIME_ATTRIBUTE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class RequestResponseLoggingFilter implements GlobalFilter, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		long startTime = System.currentTimeMillis();
		exchange.getAttributes().put(REQUEST_START_TIME_ATTRIBUTE, startTime);
		String correlationId = exchange.getAttribute(CORRELATION_ID_ATTRIBUTE);
		String method = exchange.getRequest().getMethod().name();
		String path = exchange.getRequest().getURI().getPath();
		logger.info("Incoming Request | CorrelationId={} | Method={} | Path={}", correlationId, method, path);
		return chain.filter(exchange)
				.doOnSuccess(unused -> {
					HttpStatusCode status = exchange.getResponse().getStatusCode();
					long duration = System.currentTimeMillis() - startTime;
					Object route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
					logger.info("Outgoing Response | CorrelationId={} | Route={} | Status={} | Duration={} ms",
							correlationId, route, status != null ? status.value() : "UNKNOWN", duration);
				})
				.doOnError(ex -> {
					HttpStatusCode status = exchange.getResponse().getStatusCode();
					long duration = System.currentTimeMillis() - startTime;
					Object route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
					logger.error("Request Failed | CorrelationId={} | Route={} | Status={} | Duration={} ms | Error={}",
							correlationId,route, status != null ? status.value() : "UNKNOWN", duration, ex.getMessage(), ex);
				});
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}

}