package com.storefront.inventory.dto;

public record InventoryItem(Long productId, Long sellerId, int quantity) {

}