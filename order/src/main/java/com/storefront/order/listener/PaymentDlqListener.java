package com.storefront.order.listener;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.storefront.order.dto.PaymentResult;
import com.storefront.order.service.DlqOpsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentDlqListener {

	private final DlqOpsService dlqOpsService;

    @Bean
    public Consumer<Message<PaymentResult>> paymentResultDlq() {
        return message -> {
        	log.error("PaymentResult moved to DLQ. EventId={}, OrderId={}",
                    message.getPayload().eventId(), message.getPayload().orderId());
        	dlqOpsService.savePaymentFailure(message);
        };
    }
    
}