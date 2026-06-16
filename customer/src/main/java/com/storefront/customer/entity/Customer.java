package com.storefront.customer.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter @Getter @NoArgsConstructor @AllArgsConstructor @ToString
public class Customer extends BaseEntity{

	@Id @GeneratedValue
	private Long id;
	private String name;
	private String email;
	private String password; // hashed
	private String role; 
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<Address> address;

}