package com.storefront.inventory.entity;

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
@Table(name = "inventory_dlq",
       indexes = {
           @Index(name = "idx_inventory_dlq_order",
                  columnList = "order_id"),
           @Index(name = "idx_inventory_dlq_event",
                  columnList = "event_id"),
           @Index(name = "idx_inventory_dlq_failed_at",
                  columnList = "failed_at")
       })
@Getter
@Setter
@NoArgsConstructor
public class InventoryDlq {

    @Id
    @GeneratedValue(generator = "inventory_dlq_seq")
	@SequenceGenerator(name = "inventory_dlq_seq", sequenceName = "inventory_dlq_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "event_id", nullable = false, length = 36)
    private String eventId;
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    @Column(name = "action", nullable = false)
    private String action;
    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;
    @Lob
    @Column(name = "exception_message")
    private String exceptionMessage;
    @Column(name = "failed_at", nullable = false)
    private LocalDateTime failedAt;

    public InventoryDlq(String eventId, Long orderId, String action,
    		String payload, String exceptionMessage) {
    	this.eventId = eventId;
    	this.orderId = orderId;
    	this.action = action;
    	this.payload = payload;
    	this.exceptionMessage = exceptionMessage;
    	this.failedAt = LocalDateTime.now();
    }
    
}