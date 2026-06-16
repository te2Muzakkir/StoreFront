package com.storefront.product.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.storefront.product.dto.InventoryMovementDto;
import com.storefront.product.dto.ResponseDto;

@FeignClient("inventory")
public interface InventoryFeignClient {
	
	@PostMapping("/api/inventory/add")
	public ResponseEntity<ResponseDto> addInventory(@RequestBody InventoryMovementDto inventoryMovementDto);
	
	@PostMapping("/api/inventory/remove")
	public ResponseEntity<ResponseDto> removeInventory(@RequestBody InventoryMovementDto inventoryMovementDto);

}