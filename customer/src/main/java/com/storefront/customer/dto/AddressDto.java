package com.storefront.customer.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddressDto {
	
	@NotEmpty(message = "Street can not be a null or empty")
	private String street;
	
	@NotEmpty(message = "City can not be a null or empty")
	private String city;
	
	@NotEmpty(message = "Country can not be a null or empty")
	private String country;

}