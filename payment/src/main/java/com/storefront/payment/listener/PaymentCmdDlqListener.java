package com.storefront.payment.listener;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.storefront.payment.dto.PaymentCommand;
import com.storefront.payment.service.DlqOpsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class PaymentCmdDlqListener {
	
	private final DlqOpsService dlqOpsService;
	
	@Bean
	public Consumer<Message<PaymentCommand>> paymentCommandDlq() {
	    return message -> {
	        PaymentCommand command = message.getPayload();
	        log.error("PaymentCommand moved to DLQ. EventId={}, OrderId={}, PaymentAction={}, Headers={}",
	            command.eventId(), command.orderId(), command.paymentAction(), message.getHeaders());
	        dlqOpsService.savePaymentFailure(message);
	    };
	}

}