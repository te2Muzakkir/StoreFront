package com.storefront.gatewayserver;

import static org.springframework.cloud.gateway.support.RouteMetadataUtils.CONNECT_TIMEOUT_ATTR;
import static org.springframework.cloud.gateway.support.RouteMetadataUtils.RESPONSE_TIMEOUT_ATTR;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.storefront.gatewayserver.filter.RequestSizeValidationFilter;

@SpringBootApplication
public class GatewayserverApplication {
	
	@Autowired
	private RequestSizeValidationFilter requestSizeFilter;

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}
	
	@Bean
	public RouteLocator storeFrontRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		RequestSizeValidationFilter.Config uploadLimit = new RequestSizeValidationFilter.Config();
		uploadLimit.setMaxSizeBytes(10 * 1024 * 1024);

		return routeLocatorBuilder.routes()
				.route(p -> p.path("/storefront/user/**") //Adding custom URL
						.filters(f -> f.rewritePath("/storefront/user/(?<segment>.*)" , "/${segment}")
								.circuitBreaker(c -> c.setName("userCircuitBreaker").setFallbackUri("forward:/fallback/user"))
								.retry(retryConfig -> retryConfig.setRetries(3).setMethods(HttpMethod.GET)
										.setStatuses(HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.GATEWAY_TIMEOUT)
										.setBackoff(Duration.ofSeconds(1), Duration.ofSeconds(4), 2, true)))
						.uri("lb://USER"))
				.route(p -> p.path("/storefront/product/**")
						.filters(f -> f.filter(requestSizeFilter.apply(uploadLimit))
								.rewritePath("/storefront/product/(?<segment>.*)" , "/${segment}")
								.circuitBreaker(c -> c.setName("productCircuitBreaker").setFallbackUri("forward:/fallback/product"))
								.retry(retryConfig -> retryConfig.setRetries(3).setMethods(HttpMethod.GET)
										.setStatuses(HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.GATEWAY_TIMEOUT)
										.setBackoff(Duration.ofSeconds(1), Duration.ofSeconds(4), 2, true)))
						.uri("lb://PRODUCT"))
				.route(p -> p.path("/storefront/inventory/**")
						.filters(f -> f.rewritePath("/storefront/inventory/(?<segment>.*)" , "/${segment}")
								.circuitBreaker(c -> c.setName("inventoryCircuitBreaker").setFallbackUri("forward:/fallback/inventory"))
								.retry(retryConfig -> retryConfig.setRetries(3).setMethods(HttpMethod.GET)
										.setStatuses(HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.GATEWAY_TIMEOUT)
										.setBackoff(Duration.ofSeconds(1), Duration.ofSeconds(4), 2, true)))
						.uri("lb://INVENTORY"))
				.route(p -> p.path("/storefront/payment/**")
						.filters(f -> f.rewritePath("/storefront/payment/(?<segment>.*)" , "/${segment}")
								.circuitBreaker(c -> c.setName("paymentCircuitBreaker").setFallbackUri("forward:/fallback/payment"))
								.retry(retryConfig -> retryConfig.setRetries(3).setMethods(HttpMethod.GET)
										.setStatuses(HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.GATEWAY_TIMEOUT)
										.setBackoff(Duration.ofSeconds(1), Duration.ofSeconds(4), 2, true)))
						.uri("lb://PAYMENT"))
				.route(p -> p.path("/storefront/order/**")
						.filters(f -> f.rewritePath("/storefront/order/(?<segment>.*)" , "/${segment}")
								.circuitBreaker(c -> c.setName("orderCircuitBreaker").setFallbackUri("forward:/fallback/order"))
								.retry(retryConfig -> retryConfig.setRetries(3).setMethods(HttpMethod.GET)
										.setStatuses(HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.GATEWAY_TIMEOUT)
										.setBackoff(Duration.ofSeconds(1), Duration.ofSeconds(4), 2, true)))
						.metadata(RESPONSE_TIMEOUT_ATTR, Duration.ofSeconds(60)) // route specific timeout overriding global timeout in yaml
						.metadata(CONNECT_TIMEOUT_ATTR, 5000)
						.uri("lb://ORDER")).build();
	}

}