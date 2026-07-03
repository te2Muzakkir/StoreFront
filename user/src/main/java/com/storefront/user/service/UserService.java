package com.storefront.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.storefront.user.dto.AddressDto;
import com.storefront.user.dto.AuthenticationRequest;
import com.storefront.user.dto.AuthenticationResponse;
import com.storefront.user.dto.RefreshTokenRequest;
import com.storefront.user.dto.UserDto;

import jakarta.servlet.http.HttpServletRequest;

@Service
public interface UserService {
	
	public void register(UserDto userDto);
	
	public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletRequest servletRequest);
	
	public UserDto getUser(String email);
	
	public boolean updateUser(UserDto userDto);
	
	public List<AddressDto> getAddress(String userId);
	
	public boolean addAddress(String userId, AddressDto addressDto);
	
	public boolean updateAddress(AddressDto addressDto);
	
	public boolean deleteAddress(String addressId);
	
	public AuthenticationResponse refreshToken(RefreshTokenRequest request, HttpServletRequest servletRequest);
	
	public String logout(RefreshTokenRequest request);

	public String logoutAll();

}