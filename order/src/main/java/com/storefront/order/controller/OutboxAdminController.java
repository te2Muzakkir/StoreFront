package com.storefront.order.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storefront.order.service.OutboxService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/outbox")
@RequiredArgsConstructor
public class OutboxAdminController {

    private final OutboxService outboxService;

    @PostMapping("/retry-failed")
    @Transactional
    public String retryFailed() {
    	return outboxService.retryFailed();
    }
    
}