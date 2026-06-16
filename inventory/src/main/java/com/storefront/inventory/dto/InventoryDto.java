package com.storefront.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class InventoryDto {
	
	@NotNull
	private Long productId;
	@NotNull
    private Long sellerId;
	@NotNull
	@Positive
	private int availableQuantity;

}