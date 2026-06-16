package com.storefront.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storefront.customer.dto.ResponseDto;
import com.storefront.customer.service.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/register")
	public ResponseEntity<ResponseDto> register(@Valid @RequestBody ) {
		
	}
	
	
}

POST /users/register     → Register user
POST /users/login        → Authenticate and return JWT
GET /users/{id}          → Get user details
PUT /users/{id}          → Update user
GET /users/{id}/addresses → List addresses
POST /users/{id}/addresses → Add address
