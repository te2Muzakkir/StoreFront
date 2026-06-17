package com.storefront.user.config;

public final class UserConstants {

	public UserConstants() {
		super();
	}
	
    public static final String  STATUS_201 = "201";
    public static final String  MESSAGE_201 = "User registered successfully";
    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Request processed successfully";
    public static final String  STATUS_417 = "417";
    public static final String  MESSAGE_417_UPDATE= "Update operation failed. Please try again or contact Dev team";
    public static final String  MESSAGE_417_DELETE= "Delete operation failed. Please try again or contact Dev team";
    public static final String SECRET = "very-secret-key-change-this";
    public static final long EXPIRATION_MS = 3600000;
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String CORRELATION_ID_ATTRIBUTE = "correlationId";

}