package com.storefront.payment.service;

import org.springframework.stereotype.Service;

import com.storefront.payment.dto.PaymentDto;

@Service
public interface PaymentService {

	PaymentDto getPaymentByOrderId(Long orderId);

}