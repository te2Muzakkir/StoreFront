package com.storefront.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.storefront.order.config.OrderConstants;
import com.storefront.order.dto.OrdersDto;
import com.storefront.order.dto.ResponseDto;
import com.storefront.order.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/order")
@Validated
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public ResponseEntity<ResponseDto> create(@Valid @RequestBody OrdersDto orderDto) {
		String message = orderService.create(orderDto);
		if(OrderConstants.ORDER_PRICE_UPDATED_ERR_MSG.equals(message)) {
			return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(OrderConstants.STATUS_417, OrderConstants.MESSAGE_417_CREATE));
        } else {
        	return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new ResponseDto(OrderConstants.STATUS_200, OrderConstants.MESSAGE_200));
        }
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OrdersDto> fetchOrder(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(orderService.getOrder(id));
	}
	
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<OrdersDto>> fetchOrdersByCustomer(@PathVariable("customerId") Long customerId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(orderService.getOrdersByCustomer(customerId));
	}
	
	@PutMapping
	public ResponseEntity<ResponseDto> updateStatus(@RequestParam("id") Long id,
			@RequestParam("status") String status) {
		boolean isUpdated = orderService.updateStatus(id, status);
		if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(OrderConstants.STATUS_200, OrderConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(OrderConstants.STATUS_417, OrderConstants.MESSAGE_417_UPDATE));
        }
	}

}