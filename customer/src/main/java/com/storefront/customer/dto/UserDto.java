package com.storefront.customer.dto;

import java.util.List;

import com.storefront.customer.entity.Address;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDto {
	
	@NotEmpty(message = "Name can not be a null or empty")
	private String name;
	
	@NotEmpty(message = "Email can not be a null or empty")
	@Email(message = "Invalid email format")
	private String email;
	
	@NotEmpty(message = "Password can not be a null or empty")
	@Pattern(
	        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
	        message = "Password must be at least 8 characters and include 1 uppercase letter, 1 number, and 1 special character"
	    )
	private String password;
	
	private List<Address> address;

}