package com.storefront.product.mapper;

import com.storefront.product.dto.ProductDto;
import com.storefront.product.entity.Product;

public class ProductMapper {

	public static ProductDto mapToProductDto(Product product, ProductDto productDto) {
		productDto.setActive(product.isActive());
		productDto.setCategoryId(product.getCategoryId());
		productDto.setDescription(product.getDescription());
		productDto.setName(product.getName());
		productDto.setPrice(product.getPrice());
		productDto.setSellerId(product.getSellerId());
		return productDto;
	}

	public static Product mapToProduct(ProductDto productDto, Product product) {
		product.setActive(productDto.isActive());
		product.setCategoryId(productDto.getCategoryId());
		product.setDescription(productDto.getDescription());
		product.setName(productDto.getName());
		product.setPrice(productDto.getPrice());
		product.setSellerId(productDto.getSellerId());
		return product;
	}

}