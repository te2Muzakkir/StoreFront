package com.storefront.order.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.order.config.InventoryAction;
import com.storefront.order.config.OrderConstants;
import com.storefront.order.config.OrderStatus;
import com.storefront.order.dto.InventoryCommand;
import com.storefront.order.dto.InventoryItem;
import com.storefront.order.dto.OrderItemsDto;
import com.storefront.order.dto.OrdersDto;
import com.storefront.order.dto.Product;
import com.storefront.order.entity.OrderItems;
import com.storefront.order.entity.OrderSaga;
import com.storefront.order.entity.OrderSagaItem;
import com.storefront.order.entity.Orders;
import com.storefront.order.exception.ResourceNotFoundException;
import com.storefront.order.mapper.OrdersMapper;
import com.storefront.order.metrics.OrderMetricsService;
import com.storefront.order.repository.OrderItemsRepository;
import com.storefront.order.repository.OrderRepository;
import com.storefront.order.repository.OrderSagaItemRepository;
import com.storefront.order.repository.OrderSagaRepository;
import com.storefront.order.service.client.ProductFeignClient;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemsRepository orderItemsRepository;
	
	@Autowired
	private OutboxService outboxService;
	
	@Autowired
	private OrderSagaRepository orderSagaRepository;
	
	@Autowired
	private OrderSagaItemRepository orderSagaItemRepository;
	
	@Autowired
	private ProductFeignClient productFeignClient;
	
	@Autowired
	private MeterRegistry meterRegistry;

	@Autowired
	private OrderMetricsService orderMetricsService;
	
	private boolean hasPriceChanged(List<OrderItemsDto> orderItemsDtoList) {
		for(OrderItemsDto orderItemsDto : orderItemsDtoList) {
			ResponseEntity<Product> responseEntity = productFeignClient.getProduct(String.valueOf(orderItemsDto.getProductId()));
			Product product = responseEntity.getBody();
			if(!product.getPrice().equals(orderItemsDto.getPrice()))
				return true;
		}
		return false;
	}

	@Override
	@Transactional
	public String create(OrdersDto orderDto) {
		Timer.Sample sample = Timer.start(meterRegistry);
		Orders order = new Orders();
		order.setCreatedAt(LocalDateTime.now());
		order.setCustomerId(orderDto.getCustomerId());
		order.setStatus(OrderStatus.NEW.getValue());
		List<OrderItems> orderItemsList = new ArrayList<>();
		List<OrderSagaItem> orderSagaItemList = new ArrayList<>();
		List<InventoryItem> inventoryItemList = new ArrayList<>();
		BigDecimal orderValue = orderDto.getOrderItemsDtoList().stream()
		        .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
		        .reduce(BigDecimal.ZERO, BigDecimal::add);
		if(!orderValue.equals(orderDto.getTotalAmount())) {
			orderMetricsService.incrementOrdersFailed();
			return OrderConstants.ORDER_PRICE_UPDATED_ERR_MSG;
		}
		if(hasPriceChanged(orderDto.getOrderItemsDtoList())) {
			orderMetricsService.incrementOrdersFailed();
			return OrderConstants.ORDER_PRICE_UPDATED_ERR_MSG;
		}
		order.setTotalAmount(orderValue);
		orderRepository.save(order);
		for(OrderItemsDto orderItemsDto : orderDto.getOrderItemsDtoList()) {
			OrderItems orderItems = new OrderItems();
			orderItems.setOrderId(order.getId());
			orderItems.setProductId(orderItemsDto.getProductId());
			orderItems.setSellerId(orderItemsDto.getSellerId());
			orderItems.setQuantity(orderItemsDto.getQuantity());
			orderItems.setPrice(orderItemsDto.getPrice());
			orderItemsList.add(orderItems);

			orderSagaItemList.add(new OrderSagaItem(order.getId(), 
					orderItemsDto.getProductId(), orderItemsDto.getSellerId(), orderItemsDto.getQuantity()));
			inventoryItemList.add(
					new InventoryItem(orderItemsDto.getProductId(), 
							orderItemsDto.getSellerId(), orderItemsDto.getQuantity()));
		}
		orderItemsRepository.saveAll(orderItemsList);
		orderSagaRepository.save(new OrderSaga(order.getId()));
		orderSagaItemRepository.saveAll(orderSagaItemList);
		outboxService.saveEvent("ORDER", order.getId().toString(), "inventoryCommand-out-0", 
				new InventoryCommand(UUID.randomUUID().toString(), order.getId(),
						inventoryItemList, InventoryAction.RESERVE));
		orderMetricsService.incrementOrdersCreated();
		sample.stop(orderMetricsService.getOrderProcessingTimer());
		return OrderConstants.ORDER_CREATED_SUCCESSFULLY;
	}

	@Override
	public OrdersDto getOrder(Long id) {
		Orders order = orderRepository.findById(id).orElseThrow(
						() -> new ResourceNotFoundException("Order", "id", id.toString()));
		List<OrderItems> orderItemList = orderItemsRepository.findByOrderId(id);
		return OrdersMapper.mapToOrderDto(order, orderItemList, new OrdersDto());
	}

	@Override
	public List<OrdersDto> getOrdersByCustomer(Long customerId) {
		List<OrdersDto> ordersDtoList = new ArrayList<>();
		List<Orders> orderList = orderRepository.findByCustomerId(customerId);
		for(Orders order : orderList) {
			List<OrderItems> orderItemList = orderItemsRepository.findByOrderId(order.getId());
			OrdersDto orderDto = OrdersMapper.mapToOrderDto(order, orderItemList, new OrdersDto());
			ordersDtoList.add(orderDto);
		}
		return ordersDtoList;
	}

	@Override
	public boolean updateStatus(Long id, String status) {
		boolean isUpdated = false;
		Orders order = orderRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Order", "id", id.toString()));
		order.setStatus(status);
		orderRepository.save(order);
		isUpdated = true;
		return isUpdated;
	}

}