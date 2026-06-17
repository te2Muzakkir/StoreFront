package com.storefront.order.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.storefront.order.config.OrderConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.persistence.Index;

@Entity
@Table(
    name = "outbox_event",
    indexes = {
        @Index(name = "idx_outbox_status", columnList = "status"),
        @Index(name = "idx_outbox_created_at", columnList = "created_at")
    }
)
public class OutboxEvent {

    @Id
    @Column(name = "event_id", nullable = false, length = 36)
    private String eventId;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;

    @Column(name = "last_error")
    private String lastError;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    
    @Column(name = "processing_started_at")
    private LocalDateTime processingStartedAt;

    @Version
    private Long version;

    protected OutboxEvent() {}

    public OutboxEvent(String aggregateType, String aggregateId,
            String destination, String payload) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.destination = destination;
        this.payload = payload;
        this.status = OrderConstants.OUTBOX_STATUS_PENDING;
        this.retryCount = 0;
        this.createdAt = LocalDateTime.now();
    }
    
    public void markProcessing() {
        if (!OrderConstants.OUTBOX_STATUS_PENDING.equals(this.status)) 
            throw new IllegalStateException("Only PENDING events can be processed");
        this.status = OrderConstants.OUTBOX_STATUS_PROCESSING;
        this.processingStartedAt = LocalDateTime.now();
    }

    public void markPublished() {
        this.status = OrderConstants.OUTBOX_STATUS_PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }
    
    public void resetToPending() {
        this.status = OrderConstants.OUTBOX_STATUS_PENDING;
        this.processingStartedAt = null;
    }

    public void markFailed(String error, int maxRetries) {
        this.retryCount++;
        this.lastError = error;
        if (this.retryCount >= maxRetries) 
            this.status = OrderConstants.OUTBOX_STATUS_FAILED;
        else
        	this.status = OrderConstants.OUTBOX_STATUS_PENDING;
    }

    public String getEventId() {
        return eventId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getDestination() {
        return destination;
    }

    public String getPayload() {
        return payload;
    }

    public String getStatus() {
        return status;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public String getLastError() {
        return lastError;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }
}