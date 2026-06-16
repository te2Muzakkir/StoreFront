package com.storefront.user.dto;

import lombok.Data;

@Data
public class AddressDto {
	
	private String street;
	private String city;
	private String country;
	//TODO: default, phone number, receiver name, area, landmark, pincode, state, house number/ single full address field as per Amazon 

}