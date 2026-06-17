package com.storefront.payment.config;

public final class PaymentConstants {

	private PaymentConstants() {
		super();
	}
	
    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Payment request processed successfully";
    public static final String  COMPLETED_PAYMENT_STATUS = "COMPLETED";
    public static final String  COMPLETED_PAYMENT_MESSAGE = "Payment already done, Please wait sometime for the payment to be completed";
    public static final String  PAYMENT_STATUS_417 = "417";
    public static final String  PAYMENT_MESSAGE_417 = "Payment failed. Please try again later or contact Dev team";
    public static final String  PAYMENT_STATUS_PENDING = "PENDING";
    public static final String  PAYMENT_STATUS_SUCCESS = "SUCCESS";
    public static final String  PAYMENT_STATUS_FAILED = "FAILED";
    public static final String  PAYMENT_STATUS_REFUNDED = "REFUNDED";
    public static final String INVALID_AMOUNT = "INVALID_AMOUNT";
    public static final String PAYMENT_NOT_FOUND = "PAYMENT_NOT_FOUND";
    public static final String INVALID_REFUND = "INVALID_REFUND";
    public static final String INVALID_PAYMENT_AMOUNT = "INVALID_PAYMENT_AMOUNT";
    public static final String PAYMENT_GATEWAY_DECLINED = "PAYMENT_GATEWAY_DECLINED";
    public static final String OUTBOX_STATUS_PENDING = "PENDING";
    public static final String OUTBOX_STATUS_PUBLISHED = "PUBLISHED";
    public static final String OUTBOX_STATUS_FAILED = "FAILED";
    public static final String OUTBOX_STATUS_PROCESSING = "PROCESSING";
    public static final int MAX_PUBLISH_RETRIES = 10;
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String CORRELATION_ID_ATTRIBUTE = "correlationId";
    
}