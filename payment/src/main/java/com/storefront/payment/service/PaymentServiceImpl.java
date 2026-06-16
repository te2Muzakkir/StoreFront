package com.storefront.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storefront.payment.dto.PaymentDto;
import com.storefront.payment.entity.Payment;
import com.storefront.payment.mapper.PaymentMapper;
import com.storefront.payment.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Override
	public PaymentDto getPaymentByOrderId(Long orderId) {
		Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Payment not found for order " + orderId)
                );
        return PaymentMapper.mapToProductDto(payment, new PaymentDto());
	}

}