package com.storefront.product.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Product {
	
	@Id
	@GeneratedValue(generator = "products_seq")
	@SequenceGenerator(name = "products_seq", sequenceName = "products_id_seq", allocationSize = 1)
	private Long id;
	private Long sellerId;
	private Long categoryId;
	private String name;
	private String description;
	private BigDecimal price;
	private boolean active;
	@CreatedDate
    @Column(updatable = false)
	private LocalDateTime createdAt;
	@LastModifiedDate
    @Column(insertable = false)
	private LocalDateTime updatedAt;
	
}