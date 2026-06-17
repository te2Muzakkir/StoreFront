package com.storefront.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductDto {
	
	private Long productId;
	private Long sellerId;
	private Long categoryId;
	private String name;
	private String description;
	private BigDecimal price;
	private boolean active;
	private Integer quantity;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}