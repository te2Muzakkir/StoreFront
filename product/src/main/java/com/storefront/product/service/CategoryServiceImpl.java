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
		category.setUpdatedAt(LocalDateTime.now());
		categoryRepository.save(category);
	}

	@Override
	public List<CategoryDto> getCategories() {
		return categoryRepository.findAll().stream()
				.map(category -> CategoryMapper.mapToCategoryDto(category, new CategoryDto()))
                .toList();
	}

	@Override
	public CategoryDto getCategory(String id) {
		Optional<Category> optCategory = categoryRepository.findById(Long.valueOf(id));
		if(optCategory.isEmpty())
			throw new ResourceNotFoundException("Category", "id", id);
		return CategoryMapper.mapToCategoryDto(optCategory.get(), new CategoryDto());
	}

	@Override
	public boolean update(CategoryDto categoryDto) {
		boolean isUpdated = false;
		Category category = categoryRepository.findById(categoryDto.getCategoryId()).orElseThrow(
				() -> new ResourceNotFoundException("Category", "name", categoryDto.getName()));
		category.setName(categoryDto.getName());
		category.setActive(categoryDto.isActive());
		category.setUpdatedAt(LocalDateTime.now());
		categoryRepository.save(category);
		isUpdated = true;
		return isUpdated;
	}

	@Override
	public boolean deactivate(String id) {
		boolean isDeactivate = false;
		Category category = categoryRepository.findById(Long.valueOf(id)).orElseThrow(
				() -> new ResourceNotFoundException("Category", "Id", id));
		category.setActive(false);
		categoryRepository.save(category);
		isDeactivate = true;
		return isDeactivate;
	}

}