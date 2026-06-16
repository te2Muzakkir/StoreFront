package com.storefront.order.dto;

import com.storefront.order.config.InventoryAction;

public record InventoryResult(String eventId,
		String sourceEventId,
	    Long orderId,
	    InventoryAction action,
	    boolean success,
	    String failureReason) {

}