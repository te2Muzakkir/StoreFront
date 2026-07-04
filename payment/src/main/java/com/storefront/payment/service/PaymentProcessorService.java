package com.storefront.payment.service;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.storefront.payment.dto.PaymentCommand;
import com.storefront.payment.metrics.PaymentMetricsService;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentProcessorService {
	
	private final PaymentProcessor paymentProcessor;
	private final MeterRegistry meterRegistry;
	private final PaymentMetricsService paymentMetricsService;

	@Bean
	public Consumer<PaymentCommand> paymentCommand() {
		return command -> {
			Timer.Sample sample = Timer.start(meterRegistry);
			try {
				paymentProcessor.process(command);
			}
			catch (IllegalStateException e) { // Business failures
				paymentProcessor.saveFailureResult(command, e.getMessage());
				paymentMetricsService.incrementFailed();
			} finally {
				sample.stop(paymentMetricsService.getProcessingTimer());
			}
		};
	}
    
}