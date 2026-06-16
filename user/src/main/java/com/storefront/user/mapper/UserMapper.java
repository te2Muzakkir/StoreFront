package com.storefront.user.mapper;

import java.util.ArrayList;
import java.util.List;

import com.storefront.user.dto.AddressDto;
import com.storefront.user.dto.UserDto;
import com.storefront.user.entity.Address;
import com.storefront.user.entity.User;

public class UserMapper {
	
	public static UserDto mapToUserDto(User user, UserDto userDto) {
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        List<AddressDto> addressdtoList = new ArrayList<>();
        for(Address address : user.getAddress()) {
        	AddressDto addressDto = new AddressDto();
        	addressDto.setCity(address.getCity());
        	addressDto.setCountry(address.getCountry());
        	addressDto.setStreet(address.getStreet());
        	addressdtoList.add(addressDto);
        }
        userDto.getAddress().addAll(addressdtoList);
        return userDto;
    }

    public static User mapToUser(UserDto userDto, User user) {
    	user.setName(userDto.getName());
    	user.setEmail(userDto.getEmail());
    	user.setPassword(userDto.getPassword());
    	List<Address> addressList = new ArrayList<>();
        for(AddressDto addressDto : userDto.getAddress()) {
        	Address address = new Address();
        	address.setCity(addressDto.getCity());
        	address.setCountry(addressDto.getCountry());
        	address.setStreet(addressDto.getStreet());
        	address.setUser(user);
        	addressList.add(address);
        }
        user.getAddress().addAll(addressList);
        return user;
    }

}