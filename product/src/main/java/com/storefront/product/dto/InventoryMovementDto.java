package com.storefront.product.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class InventoryMovementDto {
	
	@NotNull
	private Long productId;
	@NotNull
	private Long sellerId;
	@NotNull
	@Positive
	private int quantityChange;
	private String movementType; 
	@NotNull
	private String reference;
	private LocalDateTime createdAt;

}