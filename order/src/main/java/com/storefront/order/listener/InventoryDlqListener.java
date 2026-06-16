package com.storefront.order.listener;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.storefront.order.dto.InventoryResult;
import com.storefront.order.service.DlqOpsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryDlqListener {

	private final DlqOpsService dlqOpsService;

    @Bean
    public Consumer<Message<InventoryResult>> inventoryResultDlq() {
    	return message -> {
    		log.error("InventoryResult moved to DLQ. EventId={}, OrderId={}",
                    message.getPayload().eventId(), message.getPayload().orderId());
    		dlqOpsService.saveInventoryFailure(message);
    	};
    }
    
}