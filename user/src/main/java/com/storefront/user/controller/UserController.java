package com.storefront.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.storefront.user.config.UserConstants;
import com.storefront.user.dto.AddressDto;
import com.storefront.user.dto.ResponseDto;
import com.storefront.user.dto.UserDto;
import com.storefront.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
	
	
	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		return null;//TODO: + and - usecase
	}

	@GetMapping
	public ResponseEntity<UserDto> getUser(@RequestParam String email) {
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUser(email));
	}
	
	@PutMapping
	public ResponseEntity<ResponseDto> updateUser(@Valid @RequestBody UserDto userDto) {
		boolean isUpdated = userService.updateUser(userDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(UserConstants.STATUS_417, UserConstants.MESSAGE_417_UPDATE));
        }
	}
	
	@GetMapping("/{userId}/addresses")
	public ResponseEntity<List<AddressDto>> getAdresses(@PathVariable("userId") String userId) {
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAdresses(userId));
	}
	
	@PostMapping("/{userId}/addAddress")
	public ResponseEntity<ResponseDto> addAdresses(@PathVariable("userId") String userId, @Valid @RequestParam AddressDto addressDto) {
		boolean isUpdated = userService.addAdresses(userId, addressDto);
		 if(isUpdated) {
	            return ResponseEntity
	                    .status(HttpStatus.OK)
	                    .body(new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));
	        } else {
	            return ResponseEntity
	                    .status(HttpStatus.EXPECTATION_FAILED)
	                    .body(new ResponseDto(UserConstants.STATUS_417, UserConstants.MESSAGE_417_UPDATE));
	        }
	}
	
}