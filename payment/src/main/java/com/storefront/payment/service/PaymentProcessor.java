package com.storefront.payment.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.payment.config.PaymentAction;
import com.storefront.payment.config.PaymentConstants;
import com.storefront.payment.dto.PaymentCommand;
import com.storefront.payment.dto.PaymentResult;
import com.storefront.payment.entity.Payment;
import com.storefront.payment.entity.ProcessedEvent;
import com.storefront.payment.repository.PaymentRepository;
import com.storefront.payment.repository.ProcessedEventRepository;

@Service
public class PaymentProcessor {
	
	private final PaymentRepository paymentRepository;
    private final OutboxService outboxService;
    private final ProcessedEventRepository processedEventRepository;
	
    public PaymentProcessor(PaymentRepository paymentRepository, 
    		OutboxService outboxService, ProcessedEventRepository processedEventRepository) {
		super();
		this.paymentRepository = paymentRepository;
		this.outboxService = outboxService;
		this.processedEventRepository = processedEventRepository;
	}
    
    @Transactional
    public void process(PaymentCommand command) {
    	try {
		    processedEventRepository.saveAndFlush(
		            new ProcessedEvent(command.eventId(), LocalDateTime.now()));
		} catch (DataIntegrityViolationException e) {
		    return;
		}
        validateAmount(command.amount());
        if(PaymentAction.CHARGE.equals(command.paymentAction())) 
            processCharge(command);
        else
        	processRefund(command);
    }
    
    private void processCharge(PaymentCommand command) {
        Payment existingPayment = paymentRepository.findByOrderId(
                        command.orderId()).orElse(null);
        //Duplicate charge protection
        if (existingPayment != null && 
        		existingPayment.getStatus().equals(PaymentConstants.PAYMENT_STATUS_SUCCESS)) 
            return;
        Payment payment;
        if (existingPayment == null) {
            payment = new Payment();
            payment.setTransactionId(UUID.randomUUID().toString());
            payment.setOrderId(command.orderId());
            payment.setAmount(command.amount());
            payment.setCreatedAt(LocalDateTime.now());
        } else {
            payment = existingPayment;
        }
        payment.setStatus(PaymentConstants.PAYMENT_STATUS_PENDING);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        //Future gateway integration point
        boolean gatewaySuccess = invokePaymentGateway(payment.getTransactionId(), command.amount());
        if (gatewaySuccess) {
            payment.setStatus(PaymentConstants.PAYMENT_STATUS_SUCCESS);
            payment.setUpdatedAt(LocalDateTime.now());
            payment.setGatewayTransactionId("");
            paymentRepository.save(payment);
            sendResult(command, true, null);
            return;
        }
        payment.setStatus(PaymentConstants.PAYMENT_STATUS_FAILED);
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setGatewayTransactionId("");
        paymentRepository.save(payment);
        sendResult(command, false, PaymentConstants.PAYMENT_GATEWAY_DECLINED);
    }
    
    private void processRefund(PaymentCommand command) {
        Payment payment = paymentRepository.findByOrderId(
                        command.orderId()).orElseThrow(() ->
                        new IllegalStateException(PaymentConstants.PAYMENT_NOT_FOUND));
        //Duplicate refund protection
        if (payment.getStatus().equals(PaymentConstants.PAYMENT_STATUS_REFUNDED)) 
            return;
        if (!payment.getStatus().equals(PaymentConstants.PAYMENT_STATUS_SUCCESS)) 
            throw new IllegalStateException(PaymentConstants.INVALID_REFUND);
        // Future gateway integration point
        boolean gatewaySuccess = invokeRefundGateway(payment.getGatewayTransactionId(), payment.getAmount());
        if (!gatewaySuccess) {
        	sendResult(command, false, PaymentConstants.PAYMENT_GATEWAY_DECLINED);
            return;
        }
        payment.setStatus(PaymentConstants.PAYMENT_STATUS_REFUNDED);
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setGatewayRefundTransactionId("");
        paymentRepository.save(payment);
        sendResult(command, true, null);
    }

	private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) 
            throw new IllegalStateException(PaymentConstants.INVALID_AMOUNT);
    }
    
    /*
     * Placeholder for future Stripe/Razorpay/PayPal integration
     */
    private boolean invokePaymentGateway(String transactionId, BigDecimal amount) {
        return true;
    }

    /*
     * Placeholder for future Stripe/Razorpay/PayPal integration
     */
    private boolean invokeRefundGateway(String gatewayTransactionId, BigDecimal amount) {
        return true;
    }
    
    private void sendResult(PaymentCommand command, boolean success, String failureReason) {
    	outboxService.saveEvent("PAYMENT", command.orderId().toString(),
    	        "paymentResult-out-0",
    	        new PaymentResult(UUID.randomUUID().toString(), command.eventId(), command.orderId(),
                        command.paymentAction(), success, failureReason));
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveFailureResult(PaymentCommand command, String failureReason) {
    	outboxService.saveEvent("PAYMENT", command.orderId().toString(),
    	        "paymentResult-out-0",
	            new PaymentResult(UUID.randomUUID().toString(), command.eventId(), command.orderId(),
						command.paymentAction(), false, failureReason));
	}

}