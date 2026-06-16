package com.storefront.order.config;

public enum OrderStatus {
	NEW ("New"), 
	PAID ("Paid"), 
	CONFIRMED ("CONFIRMED"),
	SHIPPED ("Shipped"), 
	OUT_FOR_DELIVERY ("Out For Delivery"), 
	CANCELLED ("Cancelled"), 
	FAILED ("Failed"),
	INVENTORY_RESERVED ("Inventory Reserved"), 
	INVENTORY_RESERVATION_FAILED ("Inventory Reservation Failed"), 
	INVENTORY_CONFIRM_PENDING ("Inventory Confirm Pending"),
	INVENTORY_CONFIRM_FAILED ("Inventory Confirm Failed"),
	INVENTORY_RELEASE_PENDING ("Inventory Release Pending"),
	MANUAL_INTERVENTION_REQUIRED ("Manual Intervention Required"),
	PAYMENT_PENDING ("Payment Pending"),
    PAYMENT_FAILED ("Payment Failed"), 
    DELIVERED ("Delivered");
	
    private final String value;

	private OrderStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
    
}