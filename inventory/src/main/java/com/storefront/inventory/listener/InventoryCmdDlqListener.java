package com.storefront.inventory.listener;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.storefront.inventory.dto.InventoryCommand;
import com.storefront.inventory.service.DlqOpsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class InventoryCmdDlqListener {
	
	private final DlqOpsService dlqOpsService;
	
	@Bean
	public Consumer<Message<InventoryCommand>> inventoryCommandDlq() {
	    return message -> {
	        InventoryCommand command = message.getPayload();
	        log.error("InventoryCommand moved to DLQ. EventId={}, OrderId={}, Action={}, Headers={}",
	            command.eventId(), command.orderId(), command.action(), message.getHeaders());
	        dlqOpsService.saveInventoryFailure(message);
	    };
	}

}