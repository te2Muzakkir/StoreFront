package com.storefront.product.mapper;

import com.storefront.product.dto.CategoryDto;
import com.storefront.product.entity.Category;

public class CategoryMapper {
	
	private CategoryMapper() {
		super();
	}

	public static CategoryDto mapToCategoryDto(Category category, CategoryDto categoryDto) {
		categoryDto.setName(category.getName());
		categoryDto.setActive(category.isActive());
		categoryDto.setCategoryId(category.getId());
		categoryDto.setCreatedAt(category.getCreatedAt());
		categoryDto.setUpdatedAt(category.getUpdatedAt());
		return categoryDto;
	}
	
	public static Category mapToCategory(CategoryDto categoryDto, Category category) {
		category.setName(categoryDto.getName());
		category.setActive(categoryDto.isActive());
		return category;
	}

}