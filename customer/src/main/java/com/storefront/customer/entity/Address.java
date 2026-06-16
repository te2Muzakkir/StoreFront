package com.storefront.customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor @AllArgsConstructor @ToString @Getter @Setter	
public class Address extends BaseEntity {

	@Id @GeneratedValue
	private Long id;
	private String street;
	private String city;
	private String country;
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
}