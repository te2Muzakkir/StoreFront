package com.storefront.order.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dlq_audit")
@Getter
@Setter
public class DlqAudit {

    @Id
    @GeneratedValue(generator = "orders_seq")
	@SequenceGenerator(name = "orders_seq", sequenceName = "orders_id_seq", allocationSize = 1)
    private Long id;
    private String eventId;
    private String orderId;
    private String source;
    @Column(columnDefinition = "TEXT")
    private String payload;
    private String status; // PENDING / RETRIED / RESOLVED
    private Integer replayCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
}