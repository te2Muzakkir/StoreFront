package com.storefront.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storefront.user.config.UserConstants;
import com.storefront.user.dto.AuthenticationRequest;
import com.storefront.user.dto.AuthenticationResponse;
import com.storefront.user.dto.RefreshTokenRequest;
import com.storefront.user.dto.ResponseDto;
import com.storefront.user.dto.UserDto;
import com.storefront.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<ResponseDto> register(@Valid @RequestBody UserDto userDto) {
		userService.register(userDto);
		return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UserConstants.STATUS_201, UserConstants.MESSAGE_201));
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(
			@Valid @RequestBody AuthenticationRequest request, HttpServletRequest servletRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.authenticate(request, servletRequest));
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest
			, HttpServletRequest servletRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.refreshToken(refreshTokenRequest, servletRequest));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.logout(refreshTokenRequest));
	}
	
	@PostMapping("/logout-all")
	public ResponseEntity<String> logoutAll() {
		return ResponseEntity.status(HttpStatus.OK).body(userService.logoutAll());
	}

}