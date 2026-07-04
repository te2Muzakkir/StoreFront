package com.storefront.payment.metrics;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Service
public class PaymentMetricsService {

    private final Counter success;
    private final Counter failed;
    private final Counter refunded;
    private final Counter timeout;

    private final Timer processingTimer;

    public PaymentMetricsService(MeterRegistry meterRegistry) {

        success = Counter.builder("storefront.payment.success")
                .description("Successful payments")
                .register(meterRegistry);

        failed = Counter.builder("storefront.payment.failed")
                .description("Failed payments")
                .register(meterRegistry);

        refunded = Counter.builder("storefront.payment.refunded")
                .description("Refunded payments")
                .register(meterRegistry);

        timeout = Counter.builder("storefront.payment.timeout")
                .description("Payment timeout recoveries")
                .register(meterRegistry);

        processingTimer = Timer.builder("storefront.payment.processing.time")
                .description("Payment processing time")
                .publishPercentileHistogram()
                .register(meterRegistry);
    }

    public void incrementSuccess() { success.increment(); }

    public void incrementFailed() { failed.increment(); }

    public void incrementRefunded() { refunded.increment(); }

    public void incrementTimeout() { timeout.increment(); }

    public Timer getProcessingTimer() { return processingTimer; }
}