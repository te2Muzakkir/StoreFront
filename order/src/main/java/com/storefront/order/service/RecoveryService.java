package com.storefront.order.service;

import org.springframework.stereotype.Service;

import com.storefront.order.entity.OrderSaga;

@Service
public interface RecoveryService {
	
	void recoverSaga(OrderSaga orderSaga);

}