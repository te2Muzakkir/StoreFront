package com.storefront.order.dto;

import java.math.BigDecimal;

import com.storefront.order.config.PaymentAction;


public record PaymentCommand(String eventId, Long orderId, BigDecimal amount, PaymentAction paymentAction) {

}