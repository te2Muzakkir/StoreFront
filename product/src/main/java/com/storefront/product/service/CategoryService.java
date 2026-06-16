package com.storefront.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.storefront.product.dto.CategoryDto;
import com.storefront.product.entity.Category;

@Service
public interface CategoryService {
	
	public void create(CategoryDto categoryDto);
	
	public List<Category> getCategories();
	
	public Category getCategory(String id);
	
	public boolean update(CategoryDto categoryDto);
	
	public boolean delete(String id);

}