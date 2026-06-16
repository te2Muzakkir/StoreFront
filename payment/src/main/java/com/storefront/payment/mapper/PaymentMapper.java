package com.storefront.payment.mapper;

import com.storefront.payment.dto.PaymentDto;
import com.storefront.payment.entity.Payment;

public class PaymentMapper {
	
	private PaymentMapper() {
		super();
	}

	public static PaymentDto mapToProductDto(Payment payment, PaymentDto paymentDto) {
		paymentDto.setAmount(payment.getAmount());
		paymentDto.setCreatedAt(payment.getCreatedAt());
		paymentDto.setGatewayRefundTransactionId(payment.getGatewayRefundTransactionId());
		paymentDto.setGatewayTransactionId(payment.getGatewayTransactionId());
		paymentDto.setOrderId(payment.getOrderId());
		paymentDto.setStatus(payment.getStatus());
		paymentDto.setTransactionId(payment.getTransactionId());
		paymentDto.setUpdatedAt(payment.getUpdatedAt());
		return paymentDto;
	}

}