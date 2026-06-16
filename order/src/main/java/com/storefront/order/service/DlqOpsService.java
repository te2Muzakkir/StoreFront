package com.storefront.order.service;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.storefront.order.dto.InventoryResult;
import com.storefront.order.dto.PaymentResult;
import com.storefront.order.entity.OrderSagaDlq;

@Service
public interface DlqOpsService {
	
	public List<OrderSagaDlq> pendingInDlq();
	
	public void retryFromDlq(Long id);
	
	public void compensate(Long id);
	
	public void saveInventoryFailure(Message<InventoryResult> message);
	
	public void savePaymentFailure(Message<PaymentResult> message);

}