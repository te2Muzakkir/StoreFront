package com.storefront.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.storefront.order.dto.OrdersDto;

@Service
public interface OrderService {
	
	public String create(OrdersDto orderDto);
	
	public OrdersDto getOrder(Long id);
	
	public List<OrdersDto> getOrdersByCustomer(Long customerId);
	
	public boolean updateStatus(Long id, String status);

}