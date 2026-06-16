package com.storefront.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.storefront.product.dto.ProductDto;
import com.storefront.product.entity.Product;

@Service
public interface ProductService {

	public void create(ProductDto productDto);

	public List<Product> getProducts();

	public Product getProduct(String id);

	public boolean update(ProductDto productDto);

	public boolean deactivate(String id);

}