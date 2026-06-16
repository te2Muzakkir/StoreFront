package com.storefront.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.storefront.inventory.config.InventoryConstants;
import com.storefront.inventory.dto.InventoryDto;
import com.storefront.inventory.dto.InventoryMovementDto;
import com.storefront.inventory.dto.ResponseDto;
import com.storefront.inventory.service.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory")
@Validated
public class InventoryController {
	
	@Autowired
	private InventoryService inventoryService;
	
	@GetMapping
	public ResponseEntity<InventoryDto> inventory(
			@RequestParam("productId") Long productId, @RequestParam("sellerId") Long sellerId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(inventoryService.getAvaiableProductQuantity(productId, sellerId));
	}
	
	@PostMapping
	public ResponseEntity<ResponseDto> addInventory(@Valid @RequestBody InventoryMovementDto inventoryMovementDto) {
		inventoryService.addInventory(inventoryMovementDto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(InventoryConstants.STATUS_201, InventoryConstants.MESSAGE_201));
	}
	
	@PostMapping("/remove")
	public ResponseEntity<ResponseDto> removeInventory(@Valid @RequestBody InventoryMovementDto inventoryMovementDto) {
		inventoryService.removeInventory(inventoryMovementDto);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(InventoryConstants.STATUS_200, InventoryConstants.MESSAGE_200));
	}
	
	@GetMapping("/movements")
	public ResponseEntity<List<InventoryMovementDto>> movements(
			@RequestParam("productId") Long productId, @RequestParam("sellerId") Long sellerId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(inventoryService.movements(productId, sellerId));
	}
	
}