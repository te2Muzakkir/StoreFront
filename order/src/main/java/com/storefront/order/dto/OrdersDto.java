package com.storefront.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrdersDto {

	@NotNull
	private Long customerId;
	private String status;
	private BigDecimal totalAmount;
	private LocalDateTime createdAt;
	private List<OrderItemsDto> orderItemsDtoList = new ArrayList<>();
}