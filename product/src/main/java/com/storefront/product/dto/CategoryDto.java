package com.storefront.product.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CategoryDto {
	
	private String name;
	private boolean active;
	private Long categoryId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}