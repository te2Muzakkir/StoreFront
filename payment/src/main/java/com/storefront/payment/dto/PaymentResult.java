package com.storefront.payment.dto;

import com.storefront.payment.config.PaymentAction;

public record PaymentResult(String eventId, String sourceEventId, Long orderId, PaymentAction action, boolean success, String failureReason) {

}