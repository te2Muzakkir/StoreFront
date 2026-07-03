package com.storefront.product.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.product.config.ProductConstants;
import com.storefront.product.dto.InventoryMovementDto;
import com.storefront.product.dto.ProductDto;
import com.storefront.product.entity.Product;
import com.storefront.product.exception.EntityAlreadyExistsException;
import com.storefront.product.exception.ResourceNotFoundException;
import com.storefront.product.mapper.ProductMapper;
import com.storefront.product.repository.ProductRepository;
import com.storefront.product.service.client.InventoryFeignClient;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private InventoryFeignClient inventoryFeignClient;

	@Caching(evict = {
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_BY_CATEGORY, allEntries = true),
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_SEARCH, allEntries = true),
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_BY_ID, allEntries = true),
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_LOAD, allEntries = true)
		})
	@Transactional
	@Override
	public void create(ProductDto productDto) {
		Optional<Product> optProduct = productRepository.findByName(productDto.getName());
		if(optProduct.isPresent())
			throw new EntityAlreadyExistsException("Product with name: "+productDto.getName()+" already exists.");
		Product product = ProductMapper.mapToProduct(productDto, new Product());
		product.setCreatedAt(LocalDateTime.now());
		productRepository.save(product);
		InventoryMovementDto inventoryMovementDto = new InventoryMovementDto();
		inventoryMovementDto.setCreatedAt(LocalDateTime.now());
		inventoryMovementDto.setMovementType("IN");
		inventoryMovementDto.setProductId(product.getId());
		inventoryMovementDto.setQuantityChange(productDto.getQuantity());
		inventoryMovementDto.setReference("Product - "+product.getId());
		inventoryMovementDto.setSellerId(productDto.getSellerId());
		inventoryFeignClient.addInventory(inventoryMovementDto);
	}

	@Cacheable(
			cacheNames = ProductConstants.PRODUCT_LOAD,
			key = "'ALL_PRODUCTS'",
			sync = true
	)
	@Transactional
	@Override
	public List<ProductDto> getProducts() {
		return productRepository.findByActiveTrue().stream()
				.map(product -> ProductMapper.mapToProductDto(product, new ProductDto()))
				.toList();
	}

	@Cacheable(
	        cacheNames = ProductConstants.PRODUCT_BY_ID,
	        key = "#id",
	        sync = true
	)
	@Transactional
	@Override
	public ProductDto getProductById(String id) {
		Optional<Product> optProduct = productRepository.findById(Long.valueOf(id));
		if(optProduct.isEmpty())
			throw new ResourceNotFoundException("Product", "id", id);
		return ProductMapper.mapToProductDto(optProduct.get(), new ProductDto());
	}
	
	@Cacheable(
	        cacheNames = ProductConstants.PRODUCT_SEARCH,
	        key = "#text",
	        sync = true
	)
	@Transactional
	@Override
	public List<ProductDto> getProductByNameOrDescription(String text) {
		return productRepository.findByNameOrDescription(text, text).stream()
				.map(product -> ProductMapper.mapToProductDto(product, new ProductDto())).toList();
	}

	@Caching(evict = {
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_BY_CATEGORY, allEntries = true),
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_SEARCH, allEntries = true),
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_BY_ID, allEntries = true),
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_LOAD, allEntries = true)
		})
	@Transactional
	@Override
	public boolean update(ProductDto productDto) {
		boolean isUpdated = false;
		Product product = productRepository.findById(productDto.getProductId()).orElseThrow(
				() -> new ResourceNotFoundException("Product", "name", productDto.getName()));
		product = ProductMapper.mapToProduct(productDto, product);
		product.setCreatedAt(LocalDateTime.now());
		productRepository.save(product);
		InventoryMovementDto inventoryMovementDto = new InventoryMovementDto();
		inventoryMovementDto.setCreatedAt(LocalDateTime.now());
		inventoryMovementDto.setProductId(product.getId());
		inventoryMovementDto.setQuantityChange(Math.abs(productDto.getQuantity()));
		inventoryMovementDto.setReference("Product - "+product.getId());
		inventoryMovementDto.setSellerId(productDto.getSellerId());
		if(productDto.getQuantity() < 0) {
			inventoryMovementDto.setMovementType("OUT");
			inventoryFeignClient.removeInventory(inventoryMovementDto);
		} else {
			inventoryMovementDto.setMovementType("IN");
			inventoryFeignClient.addInventory(inventoryMovementDto);
		}
		isUpdated = true;
		return isUpdated;
	}

	@Caching(evict = {
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_BY_CATEGORY, allEntries = true),
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_SEARCH, allEntries = true),
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_BY_ID, allEntries = true),
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_LOAD, allEntries = true)
		})
	@Transactional
	@Override
	public boolean deactivate(String id) {
		boolean isdeactivated = false;
		Product product = productRepository.findById(Long.valueOf(id)).orElseThrow(
				() -> new ResourceNotFoundException("Product", "Id", id));
		product.setActive(false);
		productRepository.save(product);
		isdeactivated = true;
		return isdeactivated;
	}

	@Cacheable(
		    cacheNames = ProductConstants.PRODUCT_BY_CATEGORY,
		    key = "#categoryId",
		    sync = true
		)
	@Transactional
	@Override
	public List<ProductDto> findByCategory(Long categoryId) {
		List<Product> products = productRepository.findByCategoryId(categoryId);
		return products.stream()
				.map(product -> ProductMapper.mapToProductDto(product, new ProductDto()))
				.toList();
	}

	@Caching(evict = {
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_BY_CATEGORY, allEntries = true),
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_SEARCH, allEntries = true),
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_BY_ID, allEntries = true),
		    @CacheEvict(cacheNames = ProductConstants.PRODUCT_LOAD, allEntries = true)
		})
	@Transactional
	@Override
	public boolean activate(String id) {
		boolean isActivated = false;
		Product product = productRepository.findById(Long.valueOf(id)).orElseThrow(
				() -> new ResourceNotFoundException("Product", "Id", id));
		product.setActive(true);
		productRepository.save(product);
		isActivated = true;
		return isActivated;
	}

}