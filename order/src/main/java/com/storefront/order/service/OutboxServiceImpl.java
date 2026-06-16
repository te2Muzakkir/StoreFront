package com.storefront.order.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storefront.order.config.OrderConstants;
import com.storefront.order.entity.OutboxEvent;
import com.storefront.order.repository.OutboxEventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {
	
	private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
	@Override
	public void saveEvent(String aggregateType, String aggregateId, String destination, Object payload) {
    	try {
            String jsonPayload =  objectMapper.writeValueAsString(payload);
            OutboxEvent event = new OutboxEvent(aggregateType, aggregateId, destination, jsonPayload);
            outboxEventRepository.save(event);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize outbox payload", e);
        }
	}
	
	@Transactional
	@Override
    public List<OutboxEvent> claimEvents() {
        List<OutboxEvent> events = outboxEventRepository
        		.findTop100ForUpdate(OrderConstants.OUTBOX_STATUS_PENDING, PageRequest.of(0, 100));
        events.forEach(OutboxEvent::markProcessing);
        return outboxEventRepository.saveAll(events);
    }
	
	@Override
	public String retryFailed() {
		List<OutboxEvent> failedEvents = outboxEventRepository.findByStatus(OrderConstants.OUTBOX_STATUS_FAILED);
        failedEvents.forEach(OutboxEvent::resetToPending);
        return "Retry initiated";
	}

}