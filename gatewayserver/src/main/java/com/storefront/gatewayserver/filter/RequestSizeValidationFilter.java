package com.storefront.gatewayserver.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RequestSizeValidationFilter extends AbstractGatewayFilterFactory<RequestSizeValidationFilter.Config> {

	public RequestSizeValidationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			List<String> contentLengthHeaders = exchange.getRequest().getHeaders().get("Content-Length");
			if (contentLengthHeaders != null && !contentLengthHeaders.isEmpty()) {
				long contentLength = Long.parseLong(contentLengthHeaders.get(0));
				if (contentLength > config.getMaxSizeBytes()) {
					exchange.getResponse().setStatusCode(HttpStatus.CONTENT_TOO_LARGE);
					return exchange.getResponse().setComplete();
				}
			}
			return chain.filter(exchange);
		};
	}

	public static class Config {

		private long maxSizeBytes;

		public long getMaxSizeBytes() {
			return maxSizeBytes;
		}

		public void setMaxSizeBytes(long maxSizeBytes) {
			this.maxSizeBytes = maxSizeBytes;
		}
	}
	
}