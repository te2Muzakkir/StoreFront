package com.storefront.payment.service;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.storefront.payment.dto.PaymentCommand;
import com.storefront.payment.entity.PaymentDlq;

@Service
public interface DlqOpsService {
	
	public List<PaymentDlq> pendingInDlq();
	
	public void retryFromDlq(Long id);
	
	public void compensate(Long id);
	
	public void savePaymentFailure(Message<PaymentCommand> message);
	
}