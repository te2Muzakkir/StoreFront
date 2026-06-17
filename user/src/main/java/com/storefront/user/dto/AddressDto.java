package com.storefront.user.dto;

import lombok.Data;

@Data
public class AddressDto {
	
	private String address;
	private String landmark;
	private String city;
	private String state;
	private String country;
	private String pincode;
	private String phoneNumber;
	private String receiverName;
	private boolean isDefault;
	private Long addressId;

}