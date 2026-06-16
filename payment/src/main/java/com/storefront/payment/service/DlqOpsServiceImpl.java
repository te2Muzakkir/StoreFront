package com.storefront.payment.service;

import java.util.List;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storefront.payment.dto.PaymentCommand;
import com.storefront.payment.entity.PaymentDlq;
import com.storefront.payment.repository.PaymentDlqRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DlqOpsServiceImpl implements DlqOpsService {
	
	private final PaymentDlqRepository paymentDlqRepository;
	private final StreamBridge streamBridge;
	private final ObjectMapper objectMapper;

	@Override
	public List<PaymentDlq> pendingInDlq() {
		//return inventoryDlqRepository.findByProcessedFalse();
		return null;
	}

	@Override
	public void retryFromDlq(Long id) {
		try {
			PaymentDlq dlq = paymentDlqRepository.findById(id).orElseThrow();
			if ("INVENTORY".equals(dlq.getPaymentAction())) {
				PaymentCommand command =
						objectMapper.readValue(
								dlq.getPayload(),
								PaymentCommand.class
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

			if ("PAYMENT".equals(dlq.getPaymentAction())) {
				/*PaymentCommand command =
	                    objectMapper.readValue(
	                            dlq.getPayload(),
	                            PaymentCommand.class
	                    );*/

	            streamBridge.send(
	                    "paymentCommand-out-0",
	                    MessageBuilder.withPayload(null)
	                    	.setHeader("eventId", dlq.getEventId())
	                            .setHeader("orderId", dlq.getOrderId())
	                            .setHeader("retry", true)
	                            .build()
	            );
			}
			//dlq.setProcessed(true);
			paymentDlqRepository.save(dlq);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void compensate(Long id) {
		/*
		 * OrderSaga saga = orderSagaRepository.findById(id).orElseThrow(); if
		 * (saga.getStatus() == OrderSagaStatus.FAILED) return; OrderSagaDlq dlq =
		 * orderSagaDlqRepository.findById(id).orElseThrow(); //compensate
		 * //dlq.setProcessed(true); orderSagaDlqRepository.save(dlq);
		 */
		
	}
	
	@Transactional
	@Override
    public void savePaymentFailure(Message<PaymentCommand> message) {
		PaymentCommand command = message.getPayload();
        try {
            String payload = objectMapper.writeValueAsString(command);
            String exceptionMessage = getHeader(message, "x-exception-message");
            paymentDlqRepository.save(new PaymentDlq(command.eventId(), command.orderId(),
                            command.paymentAction().toString(), payload, exceptionMessage));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize InventoryCommand", e);
        }
    }

    private String getHeader(Message<?> message, String key) {
        Object value = message.getHeaders().get(key);
        return value == null ? null : value.toString();
    }

}