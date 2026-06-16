package com.storefront.product.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storefront.product.dto.CategoryDto;
import com.storefront.product.entity.Category;
import com.storefront.product.exception.EntityAlreadyExistsException;
import com.storefront.product.exception.ResourceNotFoundException;
import com.storefront.product.mapper.CategoryMapper;
import com.storefront.product.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public void create(CategoryDto categoryDto) {
		Optional<Category> optCategory = categoryRepository.findByName(categoryDto.getName());
		if(optCategory.isPresent())
			throw new EntityAlreadyExistsException("Category with name: "+categoryDto.getName()+" already exists.");
		Category category = CategoryMapper.mapToCategory(categoryDto, new Category());
		category.setCreatedAt(LocalDateTime.now());
		categoryRepository.save(category);
	}

	@Override
	public List<Category> getCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category getCategory(String id) {
		Optional<Category> optCategory = categoryRepository.findById(Long.valueOf(id));
		if(optCategory.isEmpty())
			throw new ResourceNotFoundException("Category", "id", id);
		return optCategory.get();
	}

	@Override
	public boolean update(CategoryDto categoryDto) {
		boolean isUpdated = false;
		Category category = categoryRepository.findByName(categoryDto.getName()).orElseThrow(
				() -> new ResourceNotFoundException("Category", "name", categoryDto.getName()));
		category.setName(categoryDto.getUpdatedName());
		categoryRepository.save(category);
		isUpdated = true;
		return isUpdated;
	}

	@Override
	public boolean delete(String id) {
		boolean isdeleted = false;
		Category category = categoryRepository.findById(Long.valueOf(id)).orElseThrow(
				() -> new ResourceNotFoundException("Category", "Id", id));
		categoryRepository.delete(category);
		isdeleted = true;
		return isdeleted;
	}

}