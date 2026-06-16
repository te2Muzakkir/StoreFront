package com.storefront.order.mapper;

import java.util.ArrayList;
import java.util.List;

import com.storefront.order.dto.OrderItemsDto;
import com.storefront.order.dto.OrdersDto;
import com.storefront.order.entity.OrderItems;
import com.storefront.order.entity.Orders;

public class OrdersMapper {
	
	public static OrdersDto mapToOrderDto(Orders order, List<OrderItems> orderItemsList, OrdersDto orderDto) {
		orderDto.setCustomerId(order.getCustomerId());
		orderDto.setStatus(order.getStatus());
		orderDto.setTotalAmount(order.getTotalAmount());
		List<OrderItemsDto> orderItemsDtoList = new ArrayList<>();
		for(OrderItems orderItems : orderItemsList) {
			OrderItemsDto orderItemsDto = new OrderItemsDto();
			orderItemsDto.setOrderId(orderItems.getOrderId());
			orderItemsDto.setPrice(orderItems.getPrice());
			orderItemsDto.setProductId(orderItems.getProductId());
			orderItemsDto.setQuantity(orderItems.getQuantity());
			orderItemsDto.setSellerId(orderItems.getSellerId());
			orderItemsDtoList.add(orderItemsDto);
		}
		orderDto.getOrderItemsDtoList().addAll(orderItemsDtoList);
		return orderDto;
	}

}