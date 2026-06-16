package com.storefront.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storefront.order.entity.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
	
	public List<Orders> findByCustomerId(Long id);

}