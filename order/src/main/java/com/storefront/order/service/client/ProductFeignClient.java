package com.storefront.order.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.storefront.order.dto.Product;

@FeignClient("product")
public interface ProductFeignClient {
	
	@GetMapping("/api/product/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable String id);

}