package com.storefront.gatewayserver.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class GatewayMetricsService {

    private final Counter jwtSuccess;
    private final Counter jwtFailure;
    private final Counter rateLimited;
    private final Counter routedRequests;

    public GatewayMetricsService(MeterRegistry registry) {

        jwtSuccess = Counter.builder("storefront.gateway.jwt.success")
                .description("Successful JWT validations")
                .register(registry);

        jwtFailure = Counter.builder("storefront.gateway.jwt.failure")
                .description("Failed JWT validations")
                .register(registry);

        rateLimited = Counter.builder("storefront.gateway.rate.limit")
                .description("Rate limited requests")
                .register(registry);

        routedRequests = Counter.builder("storefront.gateway.requests")
                .description("Gateway routed requests")
                .register(registry);
    }

    public void jwtSuccess() {
        jwtSuccess.increment();
    }

    public void jwtFailure() {
        jwtFailure.increment();
    }

    public void rateLimited() {
        rateLimited.increment();
    }

    public void routedRequest() {
        routedRequests.increment();
    }
}