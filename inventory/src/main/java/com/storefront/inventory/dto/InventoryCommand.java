package com.storefront.inventory.dto;

import java.util.List;

import com.storefront.inventory.config.InventoryAction;

public record InventoryCommand(String eventId, Long orderId, List<InventoryItem> inventoryItemList, InventoryAction action) {

}