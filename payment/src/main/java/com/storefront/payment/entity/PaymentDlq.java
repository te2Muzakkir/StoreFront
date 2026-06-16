package com.storefront.payment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;

@Entity
@Table(name = "payment_dlq",
indexes = {
		@Index(name = "idx_payment_dlq_order",
				columnList = "order_id"),
		@Index(name = "idx_payment_dlq_event",
		columnList = "event_id"),
		@Index(name = "idx_payment_dlq_failed_at",
		columnList = "failed_at")
})
@Getter
@Setter
@NoArgsConstructor
public class PaymentDlq {

	@Id
	@GeneratedValue(generator = "payment_dlq_seq")
	@SequenceGenerator(name = "payment_dlq_seq", sequenceName = "payment_dlq_id_seq", allocationSize = 1)
	private Long id;

	@Column(name = "event_id", nullable = false, length = 36)
	private String eventId;

	@Column(name = "order_id", nullable = false)
	private Long orderId;

	@Column(name = "payment_action", nullable = false)
	private String paymentAction;

	@Lob
	@Column(name = "payload", nullable = false)
	private String payload;

	@Lob
	@Column(name = "exception_message")
	private String exceptionMessage;

	@Column(name = "failed_at", nullable = false)
	private LocalDateTime failedAt;

	public PaymentDlq(String eventId, Long orderId, String paymentAction,
			String payload, String exceptionMessage) {
		this.eventId = eventId;
		this.orderId = orderId;
		this.paymentAction = paymentAction;
		this.payload = payload;
		this.exceptionMessage = exceptionMessage;
		this.failedAt = LocalDateTime.now();
	}

}