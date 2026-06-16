package com.storefront.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.storefront.inventory.entity.InventoryDlq;
import com.storefront.inventory.service.DlqOpsService;

@Controller
@RequestMapping("/api/order/dlq")
public class DlqOpsController {
	
	@Autowired
    private DlqOpsService dlqOpsService;

    @GetMapping
    public List<InventoryDlq> pending() {
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