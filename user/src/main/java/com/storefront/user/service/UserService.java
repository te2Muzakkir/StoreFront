package com.storefront.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.storefront.user.dto.AddressDto;
import com.storefront.user.dto.UserDto;

@Service
public interface UserService {
	
	public void register(UserDto userDto);
	
	public String verifyLogin(String email, String password);
	
	public UserDto getUser(String email);
	
	public boolean updateUser(UserDto userDto);
	
	public List<AddressDto> getAddress(String userId);
	
	public boolean addAddress(String userId, AddressDto addressDto);
	
	public boolean updateAddress(AddressDto addressDto);
	
	public boolean deleteAddress(String addressId);

}