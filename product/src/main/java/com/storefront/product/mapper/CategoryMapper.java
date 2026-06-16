package com.storefront.product.mapper;

import com.storefront.product.dto.CategoryDto;
import com.storefront.product.entity.Category;

public class CategoryMapper {
	
	public static CategoryDto mapToCategoryDto(Category category, CategoryDto categoryDto) {
		categoryDto.setName(category.getName());
		return categoryDto;
	}
	
	public static Category mapToCategory(CategoryDto categoryDto, Category category) {
		category.setName(categoryDto.getName());
		return category;
	}

}