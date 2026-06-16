package com.storefront.order.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemsDto {
	
	private Long orderId;
	@NotNull
	private Long productId;
	@NotNull
	private Long sellerId;
	private int quantity;
	private BigDecimal price;

}