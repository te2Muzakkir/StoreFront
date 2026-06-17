package com.storefront.product.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @AllArgsConstructor @ToString @NoArgsConstructor
public class Category {
	
	@Id
	@GeneratedValue(generator = "category_seq")
	@SequenceGenerator(name = "category_seq", sequenceName = "category_id_seq", allocationSize = 1)
	private Long id;
	private String name;
	private boolean active;
	@CreatedDate
    @Column(updatable = false)
	private LocalDateTime createdAt;
	@CreatedDate
    @Column(updatable = false)
	private LocalDateTime updatedAt;

}