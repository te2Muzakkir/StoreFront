package com.storefront.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor @AllArgsConstructor @ToString @Getter @Setter	
public class Address extends BaseEntity {

	@Id
	@GeneratedValue(generator="address_seq")
	@SequenceGenerator(name="address_seq",sequenceName="address_id_seq", allocationSize=1)
	private Long id;
	private String street;
	private String city;
	private String country;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}