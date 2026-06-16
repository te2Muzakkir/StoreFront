package com.storefront.order.dto;

import java.util.List;

import com.storefront.order.config.InventoryAction;

public record InventoryCommand(String eventId, Long orderId, List<InventoryItem> inventoryItemList, InventoryAction action) {

}