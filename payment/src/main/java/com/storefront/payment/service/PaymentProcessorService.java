package com.storefront.payment.service;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.storefront.payment.dto.PaymentCommand;

@Service
public class PaymentProcessorService {
	
	private final PaymentProcessor paymentProcessor;
	
    public PaymentProcessorService(PaymentProcessor paymentProcessor) {
		super();
		this.paymentProcessor = paymentProcessor;
	}

	@Bean
	public Consumer<PaymentCommand> paymentCommand() {
		return command -> {
			try {
				paymentProcessor.process(command);
			}
			catch (IllegalStateException e) { // Business failures
				paymentProcessor.saveFailureResult(command, e.getMessage());
			}
		};
	}
    
}