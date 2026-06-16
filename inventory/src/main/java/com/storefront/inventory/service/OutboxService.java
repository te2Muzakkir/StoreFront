package com.storefront.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.storefront.inventory.entity.OutboxEvent;

@Service
public interface OutboxService {
	
	public void saveEvent( String aggregateType, String aggregateId, String destination,  Object payload);
	
	public List<OutboxEvent> claimEvents();
	
	public String retryFailed();

}