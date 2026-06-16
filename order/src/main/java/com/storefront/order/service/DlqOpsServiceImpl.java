package com.storefront.order.service;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storefront.order.config.OrderConstants;
import com.storefront.order.config.OrderSagaStatus;
import com.storefront.order.dto.InventoryCommand;
import com.storefront.order.dto.InventoryResult;
import com.storefront.order.dto.PaymentCommand;
import com.storefront.order.dto.PaymentResult;
import com.storefront.order.entity.OrderSaga;
import com.storefront.order.entity.OrderSagaDlq;
import com.storefront.order.repository.OrderSagaDlqRepository;
import com.storefront.order.repository.OrderSagaRepository;

@Service
public class DlqOpsServiceImpl implements DlqOpsService {
	
	@Autowired
	private OrderSagaDlqRepository orderSagaDlqRepository;
	
	@Autowired
	private StreamBridge streamBridge;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private OrderSagaRepository orderSagaRepository;

	@Override
	public List<OrderSagaDlq> pendingInDlq() {
		return orderSagaDlqRepository.findByProcessedFalse();
	}

	@Override
	public void retryFromDlq(Long id) {
		try {
			OrderSagaDlq dlq = orderSagaDlqRepository.findById(id).orElseThrow();
			if ("INVENTORY".equals(dlq.getSource())) {
				InventoryCommand command =
						objectMapper.readValue(
								dlq.getPayload(),
								InventoryCommand.class
								);

				streamBridge.send(
						"inventoryCommand-out-0",
						MessageBuilder.withPayload(command)
						.setHeader("eventId", dlq.getEventId())
						.setHeader("orderId", dlq.getOrderId())
						.setHeader("retry", true)
						.build()
						);
			}

			if ("PAYMENT".equals(dlq.getSource())) {
				PaymentCommand command =
	                    objectMapper.readValue(
	                            dlq.getPayload(),
	                            PaymentCommand.class
	                    );

	            streamBridge.send(
	                    "paymentCommand-out-0",
	                    MessageBuilder.withPayload(command)
	                    	.setHeader("eventId", dlq.getEventId())
	                            .setHeader("orderId", dlq.getOrderId())
	                            .setHeader("retry", true)
	                            .build()
	            );
			}
			//dlq.setProcessed(true);
			orderSagaDlqRepository.save(dlq);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void compensate(Long id) {
		OrderSaga saga = orderSagaRepository.findById(id).orElseThrow();
	    if (saga.getStatus() == OrderSagaStatus.FAILED) 
	        return;
		OrderSagaDlq dlq = orderSagaDlqRepository.findById(id).orElseThrow();
		//compensate
		//dlq.setProcessed(true);
        orderSagaDlqRepository.save(dlq);
		
	}
	
	@Transactional
	@Override
    public void saveInventoryFailure(Message<InventoryResult> message) {
        save(message, OrderConstants.INVENTORY_RESULT_SOURCE,
                message.getPayload().eventId(), message.getPayload().orderId());
    }

    @Transactional
    @Override
    public void savePaymentFailure(Message<PaymentResult> message) {
        save(message, OrderConstants.PAYMENT_RESULT_SOURCE,
                message.getPayload().eventId(), message.getPayload().orderId());
    }

    private void save(Message<?> message, String source,
            String eventId, Long orderId) {
        try {
            String payload = objectMapper.writeValueAsString(message.getPayload());
            String exception =getHeader(message, "x-exception-message");
            orderSagaDlqRepository.save(new OrderSagaDlq(eventId, orderId,
                            source, payload, exception));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize DLQ payload", e);
        }
    }

    private String getHeader(Message<?> message, String key) {
        Object value = message.getHeaders().get(key);
        return value == null ? null : value.toString();
    }

}