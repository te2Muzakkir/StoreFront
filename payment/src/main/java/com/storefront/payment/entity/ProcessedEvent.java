package com.storefront.payment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "eventId"))
public class ProcessedEvent {
	
	@Id
    private String eventId;
	private LocalDateTime processedAt;
	
	public ProcessedEvent() {
		super();
	}
    
	public ProcessedEvent(String eventId, LocalDateTime processedAt) {
		super();
		this.eventId = eventId;
		this.processedAt = processedAt;
	}

	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	public LocalDateTime getProcessedAt() {
		return processedAt;
	}

	public void setProcessedAt(LocalDateTime processedAt) {
		this.processedAt = processedAt;
	}

}