package com.storefront.gatewayserver.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

	private static final String TIMESTAMP_KEY = "timestamp";
	private static final String MESSAGE_KEY = "message";
	private static final String SERVICE_KEY = "service";

	@RequestMapping(
		    value = "/fallback/user",
		    method = {
		        RequestMethod.GET,
		        RequestMethod.POST,
		        RequestMethod.PUT,
		        RequestMethod.DELETE,
		        RequestMethod.PATCH
		    }
		)
	public ResponseEntity<Map<String, Object>> userFallback() { 
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(Map.of(SERVICE_KEY, "USER", 
						MESSAGE_KEY, "User service is temporarily unavailable. Please try again later.",
						TIMESTAMP_KEY, LocalDateTime.now())); 
	} 

	@RequestMapping(
		    value = "/fallback/product",
		    method = {
		        RequestMethod.GET,
		        RequestMethod.POST,
		        RequestMethod.PUT,
		        RequestMethod.DELETE,
		        RequestMethod.PATCH
		    }
		)
	public ResponseEntity<Map<String, Object>> productFallback() { 
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(Map.of(SERVICE_KEY, "PRODUCT", 
						MESSAGE_KEY, "Product service is temporarily unavailable. Please try again later.",
						TIMESTAMP_KEY, LocalDateTime.now())); 
	} 

	@RequestMapping(
		    value = "/fallback/inventory",
		    method = {
		        RequestMethod.GET,
		        RequestMethod.POST,
		        RequestMethod.PUT,
		        RequestMethod.DELETE,
		        RequestMethod.PATCH
		    }
		)
	public ResponseEntity<Map<String, Object>> inventoryFallback() { 
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(Map.of(SERVICE_KEY, "INVENTORY", 
						MESSAGE_KEY, "Inventory service is temporarily unavailable. Please try again later.",
						TIMESTAMP_KEY, LocalDateTime.now())); 
	} 

	@RequestMapping(
		    value = "/fallback/order",
		    method = {
		        RequestMethod.GET,
		        RequestMethod.POST,
		        RequestMethod.PUT,
		        RequestMethod.DELETE,
		        RequestMethod.PATCH
		    }
		)
	public ResponseEntity<Map<String, Object>> orderFallback() { 
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(Map.of(SERVICE_KEY, "ORDER", 
						MESSAGE_KEY, "Order service is temporarily unavailable. Please try again later.",
						TIMESTAMP_KEY, LocalDateTime.now())); 
	} 

	@RequestMapping(
		    value = "/fallback/payment",
		    method = {
		        RequestMethod.GET,
		        RequestMethod.POST,
		        RequestMethod.PUT,
		        RequestMethod.DELETE,
		        RequestMethod.PATCH
		    }
		)
	public ResponseEntity<Map<String, Object>> paymentFallback() { 
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(Map.of(SERVICE_KEY, "PAYMENT", 
						MESSAGE_KEY, "Payment service is temporarily unavailable. Please try again later.",
						TIMESTAMP_KEY, LocalDateTime.now())); 
	}

}