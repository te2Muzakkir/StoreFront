package com.storefront.inventory.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.inventory.dto.InventoryDto;
import com.storefront.inventory.dto.InventoryMovementDto;
import com.storefront.inventory.entity.Inventory;
import com.storefront.inventory.entity.InventoryId;
import com.storefront.inventory.entity.InventoryMovement;
import com.storefront.inventory.repository.InventoryMovementRepository;
import com.storefront.inventory.repository.InventoryRepository;


@Service
public class InventoryServiceImpl implements InventoryService {
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Autowired
	private InventoryMovementRepository inventoryMovementRepository;

	@Override
	public InventoryDto getAvaiableProductQuantity(Long productId, Long sellerId) {
		Inventory inventory = inventoryRepository
                .findByIdProductIdAndIdSellerId(productId, sellerId)
                .orElse(null);
        int quantity = inventory != null ? inventory.getQuantity() : 0;
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setAvailableQuantity(quantity);
        inventoryDto.setProductId(productId);
        inventoryDto.setSellerId(sellerId);
        return inventoryDto;
	}

	@Override
	@Transactional
	public void addInventory(InventoryMovementDto inventoryMovementDto) {
		if (inventoryMovementDto.getQuantityChange() <= 0) 
			throw new IllegalArgumentException("Quantity must be positive");

		Inventory inventory = inventoryRepository.findByProductIdAndSellerId(
						inventoryMovementDto.getProductId(), inventoryMovementDto.getSellerId())
				.orElseGet(() -> {
					Inventory inv = new Inventory();
					inv.setId(new InventoryId(
							inventoryMovementDto.getProductId(),
							inventoryMovementDto.getSellerId()
	                ));
					inv.setQuantity(0);
					return inv;
				});
		inventory.setQuantity(inventory.getQuantity() + inventoryMovementDto.getQuantityChange());
		inventory.setUpdatedAt(LocalDateTime.now());
		inventoryRepository.save(inventory);

		InventoryMovement inventoryMovement = new InventoryMovement();
		inventoryMovement.setProductId(inventoryMovementDto.getProductId());
		inventoryMovement.setSellerId(inventoryMovementDto.getSellerId());
		inventoryMovement.setQuantityChange(inventoryMovementDto.getQuantityChange());
		inventoryMovement.setMovementType("IN");
		inventoryMovement.setReference(inventoryMovementDto.getReference());
		inventoryMovement.setCreatedAt(LocalDateTime.now());
		inventoryMovementRepository.save(inventoryMovement);
	}

	@Override
	@Transactional
	public void removeInventory(InventoryMovementDto inventoryMovementDto) {
		Inventory inventory = inventoryRepository
	            .findByProductIdAndSellerId(inventoryMovementDto.getProductId(), inventoryMovementDto.getSellerId())
	            .orElseThrow(() ->
	                    new IllegalStateException("Inventory not found")
	            );
	    if (inventory.getQuantity() < inventoryMovementDto.getQuantityChange()) {
	        throw new IllegalStateException("Insufficient stock");
	    }

	    inventory.setQuantity(inventory.getQuantity() - inventoryMovementDto.getQuantityChange());
	    inventory.setUpdatedAt(LocalDateTime.now());
	    inventoryRepository.save(inventory);
	    
	    InventoryMovement inventoryMovement = new InventoryMovement();
	    inventoryMovement.setProductId(inventoryMovementDto.getProductId());
		inventoryMovement.setSellerId(inventoryMovementDto.getSellerId());
		inventoryMovement.setQuantityChange(inventoryMovementDto.getQuantityChange());
		inventoryMovement.setMovementType("OUT");
		inventoryMovement.setReference(inventoryMovementDto.getReference());
		inventoryMovement.setCreatedAt(LocalDateTime.now());
		inventoryMovementRepository.save(inventoryMovement);
	}

	@Override
	public List<InventoryMovementDto> movements(Long productId, Long sellerId) {
		return inventoryMovementRepository
                .findByProductIdAndSellerIdOrderByCreatedAtDesc(productId, sellerId)
                .stream()
                .map(m -> {InventoryMovementDto movementDto = new InventoryMovementDto();
	                movementDto.setProductId(m.getProductId());
	                movementDto.setSellerId(sellerId);
	                movementDto.setQuantityChange(m.getQuantityChange());
	                movementDto.setMovementType(m.getMovementType());
	                movementDto.setReference(m.getReference());
	                movementDto.setCreatedAt(m.getCreatedAt());
	                return movementDto;
                })
                .toList();
	}

}