package com.storefront.inventory.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

@Service
public class InventoryMetricsService {

    private final Counter reserved;
    private final Counter released;
    private final Counter failed;
    private final Counter insufficient;
    private final Counter timeout;
    private final Counter confirmed;

    private final Timer processingTimer;

    public InventoryMetricsService(MeterRegistry meterRegistry) {

        reserved = Counter.builder("storefront.inventory.reserved")
                .description("Inventory reservations")
                .register(meterRegistry);

        released = Counter.builder("storefront.inventory.released")
                .description("Inventory releases")
                .register(meterRegistry);
        
        confirmed = Counter.builder("storefront.inventory.confirmed")
                .description("Inventory Confirmed")
                .register(meterRegistry);

        failed = Counter.builder("storefront.inventory.failed")
                .description("Inventory processing failures")
                .register(meterRegistry);

        insufficient = Counter.builder("storefront.inventory.insufficient")
                .description("Insufficient inventory")
                .register(meterRegistry);

        timeout = Counter.builder("storefront.inventory.timeout")
                .description("Inventory timeout recoveries")
                .register(meterRegistry);

        processingTimer = Timer.builder("storefront.inventory.processing.time")
                .description("Inventory processing time")
                .publishPercentileHistogram()
                .register(meterRegistry);
    }

    public void incrementReserved() { reserved.increment(); }

    public void incrementReleased() { released.increment(); }

    public void incrementFailed() { failed.increment(); }

    public void incrementInsufficient() { insufficient.increment(); }

    public void incrementTimeout() { timeout.increment(); }
    
    public void incrementConfirmed() { confirmed.increment(); }

    public Timer getProcessingTimer() { return processingTimer; }
}