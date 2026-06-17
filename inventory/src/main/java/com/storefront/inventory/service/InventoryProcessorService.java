package com.storefront.inventory.service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.storefront.inventory.config.InventoryConstants;
import com.storefront.inventory.dto.InventoryCommand;

import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryProcessorService {
	
	private InventoryProcessor inventoryProcessor;
	
    @Bean
    public Consumer<InventoryCommand> inventoryCommand() {
        return command -> {
        	int attempt = 0;
        	while (attempt < InventoryConstants.MAX_RETRIES) {
                attempt++;
                try {
                	inventoryProcessor.process(command);
                	return;
                } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
                    if (attempt >= InventoryConstants.MAX_RETRIES) 
                    	throw e;
                    sleepWithBackoff(attempt);
                } catch (IllegalStateException e) {
                	inventoryProcessor.saveFailureResult(command, e.getMessage());
                	return;
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