package com.storefront.order.config;

public final class OrderConstants {

	private OrderConstants() {
		super();
	}
	
	public static final String  STATUS_201 = "201";
    public static final String  MESSAGE_201 = "Order Added successfully";
    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Request processed successfully";
    public static final String  STATUS_417 = "417";
    public static final String  MESSAGE_417_UPDATE = "Update operation failed. Please try again or contact Dev team";
    public static final String  STATUS_202 = "202";
    public static final String  MESSAGE_202 = "Processing your Order";
    public static final String  MESSAGE_417_CREATE = "Create order operation failed. As the price of products has been updated";
    public static final String  ORDER_PRICE_UPDATED_ERR_MSG = "PRICE_UPDATED";
    public static final String  ORDER_CREATED_SUCCESSFULLY = "ORDER_CREATED_SUCCESSFULLY";
    public static final String OUTBOX_STATUS_PENDING = "PENDING";
    public static final String OUTBOX_STATUS_PUBLISHED = "PUBLISHED";
    public static final String OUTBOX_STATUS_FAILED = "FAILED";
    public static final String OUTBOX_STATUS_PROCESSING = "PROCESSING";
    public static final int MAX_PUBLISH_RETRIES = 10;
    public static final String INVENTORY_RESULT_SOURCE = "INVENTORY_RESULT";
    public static final String PAYMENT_RESULT_SOURCE = "PAYMENT_RESULT";
    public static final String PAYMENT_COMMAND_BINDING_KEY = "paymentCommand-out-0";
    public static final String INVENTORY_COMMAND_BINDING_KEY = "inventoryCommand-out-0";
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String CORRELATION_ID_ATTRIBUTE = "correlationId";
    
}