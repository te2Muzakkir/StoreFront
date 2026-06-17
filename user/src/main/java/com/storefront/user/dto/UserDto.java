package com.storefront.user.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UserDto {
	
	private String name;
	private String email;
	private String password;
	private List<AddressDto> address = new ArrayList<>();
	private boolean isActive;
	
}