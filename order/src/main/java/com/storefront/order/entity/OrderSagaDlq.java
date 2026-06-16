package com.storefront.order.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "order_saga_dlq",
indexes = {
    @Index(name = "idx_order_saga_dlq_order",
           columnList = "order_id"),

    @Index(name = "idx_order_saga_dlq_source",
           columnList = "source"),

    @Index(name = "idx_order_saga_dlq_failed_at",
           columnList = "failed_at")
})
@Getter @Setter @NoArgsConstructor @ToString
public class OrderSagaDlq {
	
	@Id
	@GeneratedValue(generator = "order_saga_dlq_seq")
	@SequenceGenerator(name = "order_saga_dlq_seq", sequenceName = "order_saga_dlq_id_seq", allocationSize = 1)
    private Long id;
	@Column(name = "event_id", nullable = false, length = 36)
	private String eventId;
	@Column(name = "order_id",  nullable = false)
    private Long orderId;
    private String source; // INVENTORY / PAYMENT
    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;
    @Lob
    @Column(name = "exception_message")
    private String exceptionMessage;
    @Column(name = "failed_at", nullable = false)
    private LocalDateTime failedAt;
    
    public OrderSagaDlq(String eventId, Long orderId, String source, String payload, String exceptionMessage) {
    	this.eventId = eventId;
    	this.orderId = orderId;
    	this.source = source;
    	this.payload = payload;
    	this.exceptionMessage = exceptionMessage;
    	this.failedAt = LocalDateTime.now();
    }
    
}