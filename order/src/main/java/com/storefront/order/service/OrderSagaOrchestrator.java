package com.storefront.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.order.config.InventoryAction;
import com.storefront.order.config.OrderSagaStatus;
import com.storefront.order.config.OrderStatus;
import com.storefront.order.config.PaymentAction;
import com.storefront.order.dto.InventoryCommand;
import com.storefront.order.dto.InventoryItem;
import com.storefront.order.dto.InventoryResult;
import com.storefront.order.dto.PaymentCommand;
import com.storefront.order.dto.PaymentResult;
import com.storefront.order.entity.OrderSaga;
import com.storefront.order.entity.OrderSagaItem;
import com.storefront.order.entity.Orders;
import com.storefront.order.entity.ProcessedEvent;
import com.storefront.order.repository.OrderRepository;
import com.storefront.order.repository.OrderSagaItemRepository;
import com.storefront.order.repository.OrderSagaRepository;
import com.storefront.order.repository.ProcessedEventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderSagaOrchestrator {
	
	private final OrderSagaRepository orderSagaRepository;
	
    private final ProcessedEventRepository processedEventRepository;
	
	private final OutboxService outboxService;
	
    private final OrderSagaItemRepository orderSagaItemRepository;
	
    private final OrderRepository orderRepository;
	
	@Bean
    public Consumer<InventoryResult> inventoryResult() {
        return this::processInventoryResult;
    }
	
	@Transactional
	public void processInventoryResult(InventoryResult result) {
		if (processedEventRepository.existsById(result.eventId())) 
			return;
		processedEventRepository.save(new ProcessedEvent(result.eventId(), LocalDateTime.now()));
		OrderSaga orderSaga = orderSagaRepository.findById(result.orderId()).orElseThrow();
		Orders order = orderRepository.findById(orderSaga.getOrderId()).orElseThrow();
		
		if (result.action() == InventoryAction.RESERVE) {
			 inventoryReserve(result, orderSaga, order);
		} else if (result.action() == InventoryAction.CONFIRM) {
			inventoryConfirm(result, orderSaga, order);
		} else if(result.action() == InventoryAction.RELEASE) {
			inventoryRelease(result, orderSaga, order);
		}
	}

	private void inventoryReserve(InventoryResult result, OrderSaga orderSaga, Orders order) {
		if (orderSaga.getStatus() != OrderSagaStatus.STARTED) 
			 return;
		if (result.success()) {
			orderSaga.inventoryReserved();
			order.setStatus(OrderStatus.INVENTORY_RESERVED.getValue());
			orderRepository.save(order);
			if (orderSaga.canSendPaymentCommand()) {
				orderSaga.setPaymentCommandSent(true);
				outboxService.saveEvent("ORDER", orderSaga.getOrderId().toString(), "paymentCommand-out-0", 
						new PaymentCommand(UUID.randomUUID().toString(), 
								orderSaga.getOrderId(), order.getTotalAmount(), PaymentAction.CHARGE));
			}
			orderSagaRepository.save(orderSaga);
		} else {
			orderSaga.failed();
			orderSagaRepository.save(orderSaga);
			order.setStatus(OrderStatus.INVENTORY_RESERVATION_FAILED.getValue());
			orderRepository.save(order);
		}
	}
	
	private void inventoryConfirm(InventoryResult result, OrderSaga orderSaga, Orders order) {
		if (orderSaga.getStatus() != OrderSagaStatus.WAITING_INVENTORY_CONFIRM) 
		 return;
		if (result.success()) {
			orderSaga.completed();
			order.setStatus(OrderStatus.CONFIRMED.getValue());
		} else {
			orderSaga.failed();
			order.setStatus(OrderStatus.INVENTORY_CONFIRM_FAILED.getValue());
		}
		orderSagaRepository.save(orderSaga);
		orderRepository.save(order);
	}
	
	private void inventoryRelease(InventoryResult result, OrderSaga orderSaga, Orders order) {
		if (orderSaga.getStatus() != OrderSagaStatus.WAITING_INVENTORY_RELEASE) 
			 return;
		if (result.success()) {
			orderSaga.failed();
			order.setStatus(OrderStatus.FAILED.getValue());
		} else {
			orderSaga.failed();
			order.setStatus(OrderStatus.INVENTORY_RESERVATION_FAILED.getValue());
		}
		orderSagaRepository.save(orderSaga);
		orderRepository.save(order);
	}
	
	@Bean
	public Consumer<PaymentResult> paymentResult() {
		return this::processPaymentResult;
	}

	@Transactional
	public void processPaymentResult(PaymentResult result) {
		if (processedEventRepository.existsById(result.eventId())) 
			return;
		processedEventRepository.save(new ProcessedEvent(result.eventId(), LocalDateTime.now()));
		OrderSaga orderSaga = orderSagaRepository.findById(result.orderId()).orElseThrow();
		Orders order = orderRepository.findById(orderSaga.getOrderId()).orElseThrow();
		
		if (orderSaga.getStatus() != OrderSagaStatus.INVENTORY_RESERVED) 
			 return;
		if (result.success()) {
			orderSaga.waitingInventoryConfirm();
			order.setStatus(OrderStatus.INVENTORY_CONFIRM_PENDING.getValue());
		    if (orderSaga.canSendPaymentCommand()) {
		    	orderSaga.setInventoryConfirmSent(true);
		    	sendInventoryAction(orderSaga, InventoryAction.CONFIRM);
		    }
		} else {
			orderSaga.waitingInventoryRelease();
		    order.setStatus(OrderStatus.INVENTORY_RELEASE_PENDING.getValue());
		    if (orderSaga.canSendInventoryRelease()) {
		    	orderSaga.setInventoryReleaseSent(true);
		    	sendInventoryAction(orderSaga, InventoryAction.RELEASE);
		    }
		}
		orderRepository.save(order);
		orderSagaRepository.save(orderSaga);
	}
	
	private void sendInventoryAction(OrderSaga orderSaga, InventoryAction action) {
		List<OrderSagaItem> orderSagaItemList = orderSagaItemRepository
        		.findByOrderId(orderSaga.getOrderId());
        List<InventoryItem> releaseItemList = orderSagaItemList.stream()
            .map(orderSagaItem -> 
            new InventoryItem(orderSagaItem.getProductId(), 
            		orderSagaItem.getSellerId(), orderSagaItem.getQuantity()))
            .toList();
        outboxService.saveEvent("ORDER", orderSaga.getOrderId().toString(), "inventoryCommand-out-0",
                new InventoryCommand(UUID.randomUUID().toString(), orderSaga.getOrderId(),
                        releaseItemList, action));
    }

}