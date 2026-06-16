package com.storefront.inventory.dto;

import com.storefront.inventory.config.InventoryAction;

public record InventoryResult(String eventId,
		String sourceEventId,
	    Long orderId,
	    InventoryAction action,
	    boolean success,
	    String failureReason) {

}