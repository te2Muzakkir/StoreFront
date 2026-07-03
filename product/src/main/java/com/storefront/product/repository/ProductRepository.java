package com.storefront.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storefront.product.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	Optional<Product> findByName(String name);
	
	List<Product> findByNameOrDescription(String name, String description);
	
	List<Product> findByActiveTrue();
	
	List<Product> findByCategoryId(Long categoryId);

}