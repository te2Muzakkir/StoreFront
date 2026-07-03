package com.storefront.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.storefront.product.dto.CategoryDto;

@Service
public interface CategoryService {
	
	public void create(CategoryDto categoryDto);
	
	public List<CategoryDto> getCategories();
	
	public CategoryDto getCategory(String id);
	
	public boolean update(CategoryDto categoryDto);
	
	public boolean deactivate(String id);
	
	public boolean activate(String id);

}