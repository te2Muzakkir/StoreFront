package com.storefront.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentDto {
	
	@NotNull
	private Long orderId;
	@Positive
	private BigDecimal amount;
	private String status;
	private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
	private String transactionId;
	private String gatewayTransactionId;
	private String gatewayRefundTransactionId;

}