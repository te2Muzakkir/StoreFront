package com.storefront.product.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductDto {
	
	private Long sellerId;
	private Long categoryId;
	private String name;
	private String description;
	private BigDecimal price;
	private boolean active;
	private Integer quantity;

}