package com.storefront.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.storefront.inventory.dto.InventoryDto;
import com.storefront.inventory.dto.InventoryMovementDto;

@Service
public interface InventoryService {
	
	public InventoryDto getAvaiableProductQuantity(Long productId, Long sellerId);
	
	public void addInventory(InventoryMovementDto inventoryMovementDto);
	
	public void removeInventory(InventoryMovementDto inventoryMovementDto);
	
	public List<InventoryMovementDto> movements(Long productId, Long sellerId);

}