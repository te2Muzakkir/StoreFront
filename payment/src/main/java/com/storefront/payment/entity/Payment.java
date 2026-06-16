package com.storefront.payment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Payment {
	
	@Id
	@GeneratedValue(generator = "payments_seq")
	@SequenceGenerator(name = "payments_seq", sequenceName = "payments_id_seq", allocationSize = 1)
	private Long id;
	private Long orderId;
	private BigDecimal amount;
	private String status;
	private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Version
    private Long version;
	private String transactionId;
	private String gatewayTransactionId;
	private String gatewayRefundTransactionId;

}