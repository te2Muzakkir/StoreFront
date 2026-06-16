package com.storefront.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storefront.order.entity.OrderSaga;
import com.storefront.order.repository.OrderSagaRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/saga")
@RequiredArgsConstructor
public class SagaAdminController {

    private final OrderSagaRepository orderSagaRepository;

    @GetMapping("/{orderId}")
    public OrderSaga getSaga(@PathVariable Long orderId) {
        return orderSagaRepository.findById(orderId).orElseThrow();
    }
}