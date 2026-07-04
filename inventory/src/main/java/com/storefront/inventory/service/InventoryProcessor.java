package com.storefront.inventory.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.inventory.config.InventoryConstants;
import com.storefront.inventory.dto.InventoryCommand;
import com.storefront.inventory.dto.InventoryItem;
import com.storefront.inventory.dto.InventoryResult;
import com.storefront.inventory.entity.Inventory;
import com.storefront.inventory.entity.InventoryMovement;
import com.storefront.inventory.entity.ProcessedEvent;
import com.storefront.inventory.metrics.InventoryMetricsService;
import com.storefront.inventory.repository.InventoryMovementRepository;
import com.storefront.inventory.repository.InventoryRepository;
import com.storefront.inventory.repository.ProcessedEventRepository;

@Service
public class InventoryProcessor {
	
	@Autowired
	private OutboxService outboxService;
	
	@Autowired
    private ProcessedEventRepository processedEventRepository;
	
	@Autowired
	private InventoryMovementRepository inventoryMovementRepository;
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Autowired
	private InventoryMetricsService inventoryMetricsService;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void process(InventoryCommand command) {
		try {
			processedEventRepository.saveAndFlush(
					new ProcessedEvent(command.eventId(), LocalDateTime.now()));
		} catch (DataIntegrityViolationException e) {
			return;
		}
		if (!inventoryMovementRepository.findByReferenceAndMovementType(
				InventoryConstants.INVENTORY_MOVEMENT_REFERENCE_PREFIX+command.orderId(), command.action().toString()).isEmpty()) 
			return;
		List<Inventory> inventoryList = new ArrayList<>();
		List<InventoryMovement> inventoryMovementList = new ArrayList<>();
		for (InventoryItem inventoryItem : command.inventoryItemList()) {
			Inventory  inventory = inventoryRepository.findByProductIdAndSellerId(
					inventoryItem.productId(), inventoryItem.sellerId())
					.orElseThrow(() -> new IllegalStateException(InventoryConstants.INVENTORY_NOT_FOUND));

			switch (command.action()) {
			case RESERVE -> 
			reserve(command, inventoryList, inventoryMovementList, inventoryItem, inventory);
			case CONFIRM -> 
			confirm(command, inventoryList, inventoryMovementList, inventoryItem, inventory);
			case RELEASE -> 
			release(command, inventoryList, inventoryMovementList, inventoryItem, inventory);
			}
		}
		inventoryRepository.saveAllAndFlush(inventoryList);
		inventoryMovementRepository.saveAll(inventoryMovementList);
		outboxService.saveEvent("INVENTORY", command.orderId().toString(),
				InventoryConstants.INVENTORY_RESULT_BINDING_KEY,
				new InventoryResult(UUID.randomUUID().toString(), command.eventId(), command.orderId(),
						command.action(), true, null));
		switch (command.action()) {
		case RESERVE -> 
		inventoryMetricsService.incrementReserved();
		case CONFIRM -> 
		inventoryMetricsService.incrementConfirmed();
		case RELEASE -> 
		inventoryMetricsService.incrementReleased();
		}
	}
	
	private void reserve(InventoryCommand command, List<Inventory> inventoryList,
			List<InventoryMovement> inventoryMovementList, InventoryItem inventoryItem, Inventory inventory) {
		if ((inventory.getQuantity() - inventory.getReservedQuantity()) < inventoryItem.quantity()) {
			inventoryMetricsService.incrementInsufficient();
			throw new IllegalStateException(InventoryConstants.INSUFFICIENT_STOCK);
		}
		inventory.setReservedQuantity(inventory.getReservedQuantity() + inventoryItem.quantity());
		inventoryList.add(inventory);
		inventoryMovementList.add(createInventoryMovement(command.orderId(), inventoryItem, command.action().toString()));
	}
	
	private void confirm(InventoryCommand command, List<Inventory> inventoryList,
			List<InventoryMovement> inventoryMovementList, InventoryItem inventoryItem, Inventory inventory) {
		if (inventory.getQuantity() < inventoryItem.quantity()) {
			inventoryMetricsService.incrementFailed();
			throw new IllegalStateException(InventoryConstants.INVALID_CONFIRM);
		}
		if (inventory.getReservedQuantity() < inventoryItem.quantity()) {
			inventoryMetricsService.incrementInsufficient();
			throw new IllegalStateException(InventoryConstants.NOT_RESERVED);
		}
		inventory.setQuantity(inventory.getQuantity() - inventoryItem.quantity());
		inventory.setReservedQuantity(inventory.getReservedQuantity() - inventoryItem.quantity());
		inventoryList.add(inventory);
		inventoryMovementList.add(createInventoryMovement(command.orderId(), inventoryItem, command.action().toString()));
	}

	private void release(InventoryCommand command, List<Inventory> inventoryList,
			List<InventoryMovement> inventoryMovementList, InventoryItem inventoryItem, Inventory inventory) {
		if (inventory.getReservedQuantity() < inventoryItem.quantity()) {
			inventoryMetricsService.incrementFailed();
			throw new IllegalStateException(InventoryConstants.INVALID_RELEASE);
		}
		inventory.setReservedQuantity(inventory.getReservedQuantity() - inventoryItem.quantity());
		inventoryList.add(inventory);
		inventoryMovementList.add(createInventoryMovement(command.orderId(), inventoryItem, command.action().toString()));
	}
	
	private InventoryMovement createInventoryMovement(Long orderId, InventoryItem inventoryItem, String movementType) {
		InventoryMovement inventoryMovement = new InventoryMovement();
		inventoryMovement.setProductId(inventoryItem.productId());
		inventoryMovement.setSellerId(inventoryItem.sellerId());
		inventoryMovement.setQuantityChange(inventoryItem.quantity());
		inventoryMovement.setMovementType(movementType);
		inventoryMovement.setReference(InventoryConstants.INVENTORY_MOVEMENT_REFERENCE_PREFIX+orderId);
		inventoryMovement.setCreatedAt(LocalDateTime.now());
		return inventoryMovement;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveFailureResult(InventoryCommand command, String failureReason) {
	    outboxService.saveEvent("INVENTORY", command.orderId().toString(),
	            InventoryConstants.INVENTORY_RESULT_BINDING_KEY,
	            new InventoryResult(UUID.randomUUID().toString(), command.eventId(), command.orderId(),
						command.action(), false, failureReason));
	}

}