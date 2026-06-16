package com.storefront.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Orders {

	@Id
	@GeneratedValue(generator = "orders_seq")
	@SequenceGenerator(name = "orders_seq", sequenceName = "orders_id_seq", allocationSize = 1)
	private Long id;
	private Long customerId;
	private String status;
	private BigDecimal totalAmount;
	private LocalDateTime createdAt;
	private String shippingAddress;
	private String billingAddress;
	private String paymentMode;
	
}