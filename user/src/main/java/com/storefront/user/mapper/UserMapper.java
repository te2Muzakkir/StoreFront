package com.storefront.user.mapper;

import java.util.List;

import com.storefront.user.dto.AddressDto;
import com.storefront.user.dto.UserDto;
import com.storefront.user.entity.Address;
import com.storefront.user.entity.User;

public class UserMapper {
	
	private UserMapper() {
		super();
	}
	
	public static AddressDto mapToAddressDto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setAddress(address.getAddress());
        addressDto.setLandmark(address.getLandmark());
        addressDto.setCity(address.getCity());
        addressDto.setState(address.getState());
        addressDto.setCountry(address.getCountry());
        addressDto.setPincode(address.getPincode());
        addressDto.setPhoneNumber(address.getPhoneNumber());
        addressDto.setReceiverName(address.getReceiverName());
        addressDto.setDefault(address.isDefault());
        addressDto.setAddressId(address.getId());
        return addressDto;
    }

    public static Address mapToAddress(AddressDto addressDto, User user) {
        Address address = new Address();
        address.setAddress(addressDto.getAddress());
        address.setLandmark(addressDto.getLandmark());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setCountry(addressDto.getCountry());
        address.setPincode(addressDto.getPincode());
        address.setPhoneNumber(addressDto.getPhoneNumber());
        address.setReceiverName(addressDto.getReceiverName());
        address.setDefault(addressDto.isDefault());
        address.setUser(user);
        return address;
    }

    public static UserDto mapToUserDto(User user, UserDto userDto) {
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setActive(user.isActive());
        List<AddressDto> addressDtoList = user.getAddress()
                .stream()
                .map(UserMapper::mapToAddressDto)
                .toList();
        userDto.getAddress().addAll(addressDtoList);
        return userDto;
    }

    public static User mapToUser(UserDto userDto, User user) {
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        List<Address> addressList = userDto.getAddress()
                .stream()
                .map(dto -> mapToAddress(dto, user))
                .toList();
        user.getAddress().addAll(addressList);
        return user;
    }

}