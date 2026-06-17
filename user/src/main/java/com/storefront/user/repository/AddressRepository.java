package com.storefront.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storefront.user.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
	
}