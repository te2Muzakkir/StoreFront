package com.storefront.user.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.storefront.user.config.UserConstants;
import com.storefront.user.dto.AddressDto;
import com.storefront.user.dto.UserDto;
import com.storefront.user.entity.Address;
import com.storefront.user.entity.User;
import com.storefront.user.exception.ResourceNotFoundException;
import com.storefront.user.exception.UserAlreadyExistsException;
import com.storefront.user.mapper.UserMapper;
import com.storefront.user.repository.AddressRepository;
import com.storefront.user.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AddressRepository addressRepository;

	@Override
	public void register(UserDto userDto) {
		Optional<User> optUser =  userRepository.findByEmail(userDto.getEmail());
		if(optUser.isPresent())
			throw new UserAlreadyExistsException("User already registered with given email "
                    +userDto.getEmail());
		User user = UserMapper.mapToUser(userDto, new User());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("CUSTOMER");
		userRepository.save(user);	
	}

	@Override
	public String verifyLogin(String email, String password) {
		User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
		if(!user.isActive())
			throw new IllegalArgumentException("User account blocked");
	    if (!passwordEncoder.matches(password, user.getPassword()))
	        throw new IllegalArgumentException("Invalid credentials");
		return generateToken(user);
	}
	
	private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + UserConstants.EXPIRATION_MS))
                .signWith(Keys.hmacShaKeyFor(UserConstants.SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

	@Override
	public UserDto getUser(String email) {
		User user =  userRepository.findByEmail(email).orElseThrow(
				() -> new ResourceNotFoundException("User", "email", email));
		return UserMapper.mapToUserDto(user, new UserDto());
	}

	@Override
	public boolean updateUser(UserDto userDto) {
		boolean isUpdated = false;
		User user =  userRepository.findByEmail(userDto.getEmail()).orElseThrow(
				() -> new ResourceNotFoundException("User", "email", userDto.getEmail()));
		user.setEmail(userDto.getEmail());
		user.setName(userDto.getName());
		userRepository.save(user);
		isUpdated = true;
		return isUpdated;
	}

	@Override
	public List<AddressDto> getAddress(String userId) {
		User user =  userRepository.findById(Long.valueOf(userId)).orElseThrow(
				() -> new ResourceNotFoundException("User", "Id", userId));
		return UserMapper.mapToUserDto(user, new UserDto()).getAddress();
	}

	@Override
	public boolean addAddress(String userId, AddressDto addressDto) {
		boolean isUpdated = false;
		User user =  userRepository.findById(Long.valueOf(userId)).orElseThrow(
				() -> new ResourceNotFoundException("User", "Id", userId));
		Address address = UserMapper.mapToAddress(addressDto, user);
		user.getAddress().add(address);
		userRepository.save(user);
		isUpdated = true;
		return isUpdated;
	}

	@Override
	public boolean updateAddress(AddressDto addressDto) {
		boolean isUpdated = false;
		Address address = addressRepository.findById(addressDto.getAddressId()).orElseThrow(
				() -> new ResourceNotFoundException("Address", "id", String.valueOf(addressDto.getAddressId())));
		address.setAddress(addressDto.getAddress());
        address.setLandmark(addressDto.getLandmark());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setCountry(addressDto.getCountry());
        address.setPincode(addressDto.getPincode());
        address.setPhoneNumber(addressDto.getPhoneNumber());
        address.setReceiverName(addressDto.getReceiverName());
        address.setDefault(addressDto.isDefault());
        addressRepository.save(address);
        isUpdated = true;
		return isUpdated;
	}

	@Override
	public boolean deleteAddress(String addressId) {
		boolean isdeleted = false;
		Address address = addressRepository.findById(Long.valueOf(addressId)).orElseThrow(
				() -> new ResourceNotFoundException("Address", "id", addressId));
		addressRepository.delete(address);
		isdeleted = true;
		return isdeleted;
	}

}