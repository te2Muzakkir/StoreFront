package com.storefront.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storefront.payment.dto.PaymentDto;
import com.storefront.payment.service.PaymentService;

@RestController
@Validated
@RequestMapping("/api/payment")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@GetMapping("/{orderId}")
	public ResponseEntity<PaymentDto> getPayment(@PathVariable Long orderId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(paymentService.getPaymentByOrderId(orderId));
	}

}