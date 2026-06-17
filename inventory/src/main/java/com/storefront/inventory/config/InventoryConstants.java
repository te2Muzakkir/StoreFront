package com.storefront.inventory.config;

public final class InventoryConstants {

	private InventoryConstants() {
		super();
	}
	
	public static final String INVENTORY_MOVEMENT_REFERENCE_PREFIX = "Order - ";
	public static final String  STATUS_201 = "201";
    public static final String  MESSAGE_201 = "Inventory added successfully";
    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Request processed successfully";
    public static final String  STATUS_417 = "417";
    public static final String  MESSAGE_417_UPDATE= "Update operation failed. Please try again or contact Dev team";
    public static final String  MESSAGE_417_DELETE= "Delete operation failed. Please try again or contact Dev team";
    public static final int MAX_RETRIES = 3;
    public static final String INSUFFICIENT_STOCK = "INSUFFICIENT_STOCK";
    public static final String INVENTORY_NOT_FOUND = "INVENTORY_NOT_FOUND";
    public static final String NOT_RESERVED =  "NOT_RESERVED";
    public static final String INVALID_CONFIRM = "INVALID_CONFIRM";
    public static final String INVALID_RELEASE = "INVALID_RELEASE";
    public static final String INVENTORY_RESULT_BINDING_KEY = "inventoryResult-out-0";
    public static final String OUTBOX_STATUS_PENDING = "PENDING";
    public static final String OUTBOX_STATUS_PUBLISHED = "PUBLISHED";
    public static final String OUTBOX_STATUS_FAILED = "FAILED";
    public static final String OUTBOX_STATUS_PROCESSING = "PROCESSING";
    public static final int MAX_PUBLISH_RETRIES = 10;
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String CORRELATION_ID_ATTRIBUTE = "correlationId";

}