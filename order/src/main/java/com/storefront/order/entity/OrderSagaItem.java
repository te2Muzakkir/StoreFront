package com.storefront.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "order_saga_item",
    uniqueConstraints = @UniqueConstraint(columnNames = {"orderId", "productId"})
)
public class OrderSagaItem {
	
	@Id
	@GeneratedValue(generator = "order_saga_item_seq")
	@SequenceGenerator(name = "order_saga_item_seq", sequenceName = "order_saga_item_id_seq", allocationSize = 1)
    private Long id;
    private Long orderId;
    private Long productId;
    private Long sellerId;
	private int quantity;
    private boolean reserved;

    public OrderSagaItem() {
    	super();
    }

    public OrderSagaItem(Long orderId, Long productId, Long sellerId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.sellerId = sellerId;
        this.quantity = quantity;
        this.reserved = false;
    }

	public Long getId() {
		return id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getProductId() {
		return productId;
	}
	
    public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public int getQuantity() {
		return quantity;
	}

	public boolean isReserved() {
		return reserved;
	}
	
	public void markReserved() { 
		this.reserved = true; 
	}
	
    public void markReleased() { 
    	this.reserved = false; 
    }
    
}