package com.storefront.gatewayserver.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

	private static final String TIMESTAMP_KEY = "timestamp";
	private static final String MESSAGE_KEY = "message";
	private static final String SERVICE_KEY = "service";

	@GetMapping("/fallback/user") 
	public ResponseEntity<Map<String, Object>> userFallback() { 
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(Map.of(SERVICE_KEY, "USER", 
						MESSAGE_KEY, "User service is temporarily unavailable. Please try again later.",
						TIMESTAMP_KEY, LocalDateTime.now())); 
	} 

	@GetMapping("/fallback/product") 
	public ResponseEntity<Map<String, Object>> productFallback() { 
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(Map.of(SERVICE_KEY, "PRODUCT", 
						MESSAGE_KEY, "Product service is temporarily unavailable. Please try again later.",
						TIMESTAMP_KEY, LocalDateTime.now())); 
	} 

	@GetMapping("/fallback/inventory") 
	public ResponseEntity<Map<String, Object>> inventoryFallback() { 
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(Map.of(SERVICE_KEY, "INVENTORY", 
						MESSAGE_KEY, "Inventory service is temporarily unavailable. Please try again later.",
						TIMESTAMP_KEY, LocalDateTime.now())); 
	} 

	@GetMapping("/fallback/order") 
	public ResponseEntity<Map<String, Object>> orderFallback() { 
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(Map.of(SERVICE_KEY, "ORDER", 
						MESSAGE_KEY, "Order service is temporarily unavailable. Please try again later.",
						TIMESTAMP_KEY, LocalDateTime.now())); 
	} 

	@GetMapping("/fallback/payment") 
	public ResponseEntity<Map<String, Object>> paymentFallback() { 
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(Map.of(SERVICE_KEY, "PAYMENT", 
						MESSAGE_KEY, "Payment service is temporarily unavailable. Please try again later.",
						TIMESTAMP_KEY, LocalDateTime.now())); 
	}

}