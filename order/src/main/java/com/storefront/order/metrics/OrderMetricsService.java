package com.storefront.order.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

@Service
public class OrderMetricsService {

    private final Counter ordersCreated;
    private final Counter ordersCompleted;
    private final Counter ordersCancelled;
    private final Counter ordersFailed;
    private final Counter ordersCompensated;
    private final Counter orderTimeouts;

    private final Timer orderProcessingTimer;

    public OrderMetricsService(MeterRegistry meterRegistry) {

        this.ordersCreated = Counter.builder("storefront.orders.created")
                .description("Total orders created")
                .register(meterRegistry);

        this.ordersCompleted = Counter.builder("storefront.orders.completed")
                .description("Total completed orders")
                .register(meterRegistry);

        this.ordersCancelled = Counter.builder("storefront.orders.cancelled")
                .description("Total cancelled orders")
                .register(meterRegistry);

        this.ordersFailed = Counter.builder("storefront.orders.failed")
                .description("Total failed orders")
                .register(meterRegistry);

        this.ordersCompensated = Counter.builder("storefront.orders.compensated")
                .description("Total compensated orders")
                .register(meterRegistry);

        this.orderTimeouts = Counter.builder("storefront.orders.timeout")
                .description("Total order timeout recoveries")
                .register(meterRegistry);

        this.orderProcessingTimer = Timer.builder("storefront.orders.processing.time")
                .description("Order creation processing time")
                .publishPercentileHistogram()
                .register(meterRegistry);
    }

    public void incrementOrdersCreated() {
        ordersCreated.increment();
    }

    public void incrementOrdersCompleted() {
        ordersCompleted.increment();
    }

    public void incrementOrdersCancelled() {
        ordersCancelled.increment();
    }

    public void incrementOrdersFailed() {
        ordersFailed.increment();
    }

    public void incrementOrdersCompensated() {
        ordersCompensated.increment();
    }

    public void incrementTimeouts() {
        orderTimeouts.increment();
    }

    public Timer getOrderProcessingTimer() {
        return orderProcessingTimer;
    }
}