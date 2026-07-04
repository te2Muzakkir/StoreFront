package com.storefront.inventory.service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.storefront.inventory.config.InventoryConstants;
import com.storefront.inventory.dto.InventoryCommand;
import com.storefront.inventory.metrics.InventoryMetricsService;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryProcessorService {
	
	private InventoryProcessor inventoryProcessor;
	private MeterRegistry meterRegistry;
	private InventoryMetricsService inventoryMetricsService;
	
    @Bean
    public Consumer<InventoryCommand> inventoryCommand() {
        return command -> {
        	int attempt = 0;
        	Timer.Sample sample = Timer.start(meterRegistry);
        	while (attempt < InventoryConstants.MAX_RETRIES) {
                attempt++;
                try {
                	inventoryProcessor.process(command);
                	return;
                } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
                    if (attempt >= InventoryConstants.MAX_RETRIES) {
                    	inventoryMetricsService.incrementFailed();
                    	throw e;
                    }
                    sleepWithBackoff(attempt);
                } catch (IllegalStateException e) {
                	inventoryProcessor.saveFailureResult(command, e.getMessage());
                	inventoryMetricsService.incrementFailed();
                	return;
                } finally {
                	sample.stop(inventoryMetricsService.getProcessingTimer());
				}
        	}
        };
    }

	private void sleepWithBackoff(int attempt) {
        long base = 50L * (1L << attempt);
        long jitter = ThreadLocalRandom.current().nextLong(20);
        try {
            Thread.sleep(base + jitter);
        } catch (InterruptedException ignored) {
        }
    }

}