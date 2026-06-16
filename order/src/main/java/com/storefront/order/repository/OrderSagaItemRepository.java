package com.storefront.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storefront.order.entity.OrderSagaItem;

@Repository
public interface OrderSagaItemRepository extends JpaRepository<OrderSagaItem, Long> {
	
	List<OrderSagaItem> findByOrderId(Long orderId);

}