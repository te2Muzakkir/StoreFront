package com.storefront.payment.dto;

import java.math.BigDecimal;

import com.storefront.payment.config.PaymentAction;

public record PaymentCommand(String eventId, Long orderId, BigDecimal amount, PaymentAction paymentAction) {

}