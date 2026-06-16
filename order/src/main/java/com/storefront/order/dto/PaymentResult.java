package com.storefront.order.dto;

public record PaymentResult(String eventId, Long orderId, boolean success, String failureReason) {

}