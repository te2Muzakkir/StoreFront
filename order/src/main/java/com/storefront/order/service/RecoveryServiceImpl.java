package com.storefront.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.order.config.InventoryAction;
import com.storefront.order.config.OrderConstants;
import com.storefront.order.config.OrderSagaStatus;
import com.storefront.order.config.OrderStatus;
import com.storefront.order.config.PaymentAction;
import com.storefront.order.dto.InventoryCommand;
import com.storefront.order.dto.InventoryItem;
import com.storefront.order.dto.PaymentCommand;
import com.storefront.order.entity.OrderSaga;
import com.storefront.order.entity.OrderSagaItem;
import com.storefront.order.entity.Orders;
import com.storefront.order.repository.OrderRepository;
import com.storefront.order.repository.OrderSagaItemRepository;
import com.storefront.order.repository.OrderSagaRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class RecoveryServiceImpl implements RecoveryService {
	
	private static final int MAX_RECOVERY_RETRY = 5;
	
	private final OrderSagaRepository orderSagaRepository;
	private final OrderRepository orderRepository;
	private final OutboxService outboxService;
    private final OrderSagaItemRepository orderSagaItemRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void recoverSaga(OrderSaga orderSaga) {
		if (orderSaga.getStatus() == OrderSagaStatus.COMPLETED ||
				orderSaga.getStatus() == OrderSagaStatus.FAILED)
		 return;

		if (orderSaga.getUpdatedAt() != null &&
				orderSaga.getUpdatedAt().isAfter(LocalDateTime.now().minusMinutes(5)))
		 return;

		//Recovery exhausted
		if (orderSaga.getRecoveryRetryCount() >= MAX_RECOVERY_RETRY) {
			log.error("Saga recovery exhausted orderId={}", orderSaga.getOrderId());
			Orders order = orderRepository.findById(orderSaga.getOrderId()).orElseThrow();
			orderSaga.manualInterventionRequired();
			order.setStatus(OrderStatus.MANUAL_INTERVENTION_REQUIRED.getValue());
			orderSagaRepository.save(orderSaga);
			orderRepository.save(order);
			return;
		}
		retryEvent(orderSaga);
	}
	
	private void retryEvent(OrderSaga orderSaga) {
		log.info("Recovering stuck saga orderId={} status={}", orderSaga.getOrderId(), orderSaga.getStatus());
		String logMsg = "Saga recovery triggered. orderId={} status={}";
		switch (orderSaga.getStatus()) {
			// Inventory reserved but payment result never came
			case INVENTORY_RESERVED -> {
				if (!orderSaga.canSendPaymentCommand()) 
			        return;
				Orders order = orderRepository .findById(orderSaga.getOrderId()) .orElseThrow();
				orderSaga.setPaymentCommandSent(true);
				orderSaga.updateRetryCount();
				orderSagaRepository.save(orderSaga);
				log.warn(logMsg, orderSaga.getOrderId(), orderSaga.getStatus());
				outboxService.saveEvent("ORDER", orderSaga.getOrderId().toString(), OrderConstants.PAYMENT_COMMAND_BINDING_KEY, 
					    new PaymentCommand(UUID.randomUUID().toString(), orderSaga.getOrderId(),
					        order.getTotalAmount(), PaymentAction.CHARGE));
			}
			// Payment success but inventory confirm result missing
			case WAITING_INVENTORY_CONFIRM -> {
				if (!orderSaga.canSendInventoryConfirm()) 
			        return;
				orderSaga.setInventoryConfirmSent(true);
				orderSaga.updateRetryCount();
				orderSagaRepository.save(orderSaga);
				log.warn(logMsg, orderSaga.getOrderId(), orderSaga.getStatus());
				sendInventoryAction(orderSaga, InventoryAction.CONFIRM);
			}
			// Payment failed but inventory release result missing
			case WAITING_INVENTORY_RELEASE -> {
				if (!orderSaga.canSendInventoryRelease()) 
			        return;
				orderSaga.setInventoryReleaseSent(true);
				orderSaga.updateRetryCount();
				orderSagaRepository.save(orderSaga);
				log.warn(logMsg, orderSaga.getOrderId(), orderSaga.getStatus());
				sendInventoryAction(orderSaga, InventoryAction.RELEASE);
			}
			default -> {
				return;
			}
		}
	}

	private void sendInventoryAction(OrderSaga orderSaga, InventoryAction inventoryAction) {
		List<OrderSagaItem> orderSagaItemList = orderSagaItemRepository
				.findByOrderId(orderSaga.getOrderId());
		List<InventoryItem> inventoryItemList = orderSagaItemList.stream()
		    .map(orderSagaItem -> 
		    new InventoryItem(orderSagaItem.getProductId(), 
		    		orderSagaItem.getSellerId(), orderSagaItem.getQuantity()))
		    .toList();
		outboxService.saveEvent("ORDER", orderSaga.getOrderId().toString(), OrderConstants.INVENTORY_COMMAND_BINDING_KEY,
			    new InventoryCommand(UUID.randomUUID().toString(), orderSaga.getOrderId(),
			        inventoryItemList, inventoryAction));
	}

}