package com.storefront.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storefront.order.entity.OrderSagaDlq;

@Repository
public interface OrderSagaDlqRepository extends JpaRepository<OrderSagaDlq, Long> {
	
	List<OrderSagaDlq> findByProcessedFalse();

}