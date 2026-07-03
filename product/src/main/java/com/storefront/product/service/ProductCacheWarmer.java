package com.storefront.product.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProductCacheWarmer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCacheWarmer.class);

    private final ProductService productService;

    public ProductCacheWarmer(ProductService productService) {
        this.productService = productService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void warmProductCache() {
    	try {
    		LOGGER.info("Starting Product cache warming...");
    		long start = System.currentTimeMillis();
    		productService.getProducts();
    		long end = System.currentTimeMillis();
    		LOGGER.info("Product cache warming completed in {} ms", (end - start));
    	} catch (Exception e) {
    		LOGGER.warn("Product cache warming skipped. The cache will be populated on demand.", e);
    	}
    }
    
}