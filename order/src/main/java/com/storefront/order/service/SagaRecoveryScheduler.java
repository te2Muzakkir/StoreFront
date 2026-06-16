package com.storefront.order.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.order.config.OrderSagaStatus;
import com.storefront.order.entity.OrderSaga;
import com.storefront.order.repository.OrderSagaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SagaRecoveryScheduler {

	private final RecoveryService recoveryService;
	private final OrderSagaRepository orderSagaRepository;

	@Scheduled(fixedDelay = 60000)
	@Transactional
	public void recoverStuckSagas() {

		List<OrderSaga> orderSagaList = orderSagaRepository.findByStatusInAndUpdatedAtBefore(
                List.of(
                		OrderSagaStatus.INVENTORY_RESERVED,
                		OrderSagaStatus.WAITING_INVENTORY_CONFIRM,
                		OrderSagaStatus.WAITING_INVENTORY_RELEASE
                ),
                LocalDateTime.now().minusMinutes(5)
        );
		orderSagaList.forEach(recoveryService::recoverSaga);
	}


}