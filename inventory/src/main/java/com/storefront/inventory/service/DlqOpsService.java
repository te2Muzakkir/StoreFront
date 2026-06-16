package com.storefront.inventory.service;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.storefront.inventory.dto.InventoryCommand;
import com.storefront.inventory.entity.InventoryDlq;

@Service
public interface DlqOpsService {
	
	public List<InventoryDlq> pendingInDlq();
	
	public void retryFromDlq(Long id);
	
	public void compensate(Long id);
	
	public void saveInventoryFailure(Message<InventoryCommand> message);
	
}