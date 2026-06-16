package com.storefront.payment.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.storefront.payment.entity.PaymentDlq;
import com.storefront.payment.service.DlqOpsService;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/order/dlq")
@AllArgsConstructor
public class DlqOpsController {
	
    private final DlqOpsService dlqOpsService;

    @GetMapping
    public List<PaymentDlq> pending() {
        return dlqOpsService.pendingInDlq();
    }

    @PostMapping("/{id}/retry")
    public void retry(@PathVariable Long id) {
    	dlqOpsService.retryFromDlq(id);
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
    	dlqOpsService.compensate(id);
    }

}