package com.storefront.product.metrics;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class ProductMetricsService {
	
	private final Counter created;
	private final Counter updated;
	private final Counter activated;
	private final Counter deactivated;
	
	public ProductMetricsService(MeterRegistry meterRegistry) {
		
		created = Counter.builder("storefront.product.created")
				.description("Product Created").register(meterRegistry);
		
		updated = Counter.builder("storefront.product.updated")
				.description("Product Updated").register(meterRegistry);
		
		activated = Counter.builder("storefront.product.activated")
				.description("Product Activated").register(meterRegistry);
		
		deactivated = Counter.builder("storefront.product.deactivated")
				.description("Product Deactivated").register(meterRegistry);
		
	}
	
	public void incrementCreated() { created.increment(); }
	
	public void incrementUpdated() { updated.increment(); }
	
	public void incrementActivated() { activated.increment(); }
	
	public void incrementDeactivated() { deactivated.increment(); }

}