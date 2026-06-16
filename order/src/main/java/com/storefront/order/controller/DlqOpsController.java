package com.storefront.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.storefront.order.entity.OrderSagaDlq;
import com.storefront.order.service.DlqOpsService;

@Controller
@RequestMapping("/api/order/dlq")
public class DlqOpsController {
	
	@Autowired
    private DlqOpsService dlqOpsService;

    @GetMapping
    public List<OrderSagaDlq> pending() {
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