package com.storefront.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.storefront.product.dto.ProductDto;

@Service
public interface ProductService {

	public void create(ProductDto productDto);

	public List<ProductDto> getProducts();

	public ProductDto getProductById(String id);
	
	public List<ProductDto> getProductByNameOrDescription(String text);

	public boolean update(ProductDto productDto);

	public boolean deactivate(String id);
	
	public List<ProductDto> findByCategory(Long categoryId);
	
	public boolean activate(String id);

}