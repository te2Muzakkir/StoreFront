package com.storefront.user.config;

public final class UserConstants {

	private UserConstants() {
		super();
	}
	
    public static final String  STATUS_201 = "201";
    public static final String  MESSAGE_201 = "User registered successfully";
    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Request processed successfully";
    public static final String  STATUS_417 = "417";
    public static final String  MESSAGE_417_UPDATE= "Update operation failed. Please try again or contact Dev team";
    public static final String  MESSAGE_417_DELETE= "Delete operation failed. Please try again or contact Dev team";
    public static final long EXPIRATION_MS = 3600000;
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String CORRELATION_ID_ATTRIBUTE = "correlationId";
    public static final String LOGIN_FAILURE_REASON_INVALID_USERNAME = "INVALID_USERNAME";
    public static final String LOGIN_FAILURE_REASON_INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String LOGIN_FAILURE_REASON_ACCOUNT_DISABLED = "ACCOUNT_DISABLED";
    public static final String LOGIN_FAILURE_REASON_ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
    public static final String LOGIN_FAILURE_REASON_INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    public static final String LOGIN_FAILURE_REASON_LOGIN_ERROR = "LOGIN_ERROR";
    
    public static final String TOKEN_STATUS_ACTIVE = "ACTIVE";
    public static final String TOKEN_STATUS_REVOKED = "REVOKED";
    public static final String TOKEN_STATUS_EXPIRED = "EXPIRED";
    public static final String TOKEN_STATUS_ROTATED = "ROTATED";
    public static final String HASH_ALGORITHM = "SHA-256";
    public static final String SECURITY_TOKEN_TYPE_BEARER = "Bearer";
    
    public static final String SECURITY_AUDIT_EVENT_LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static final String SECURITY_AUDIT_EVENT_LOGIN_FAILED = "LOGIN_FAILED";
    public static final String SECURITY_AUDIT_EVENT_LOGOUT = "LOGOUT";
    public static final String SECURITY_AUDIT_EVENT_LOGOUT_ALL = "LOGOUT_ALL";
    public static final String SECURITY_AUDIT_EVENT_REFRESH_SUCCESS = "REFRESH_SUCCESS";
    public static final String SECURITY_AUDIT_EVENT_REFRESH_REUSE_DETECTED = "REFRESH_REUSE_DETECTED";
    public static final String SECURITY_AUDIT_EVENT_REFRESH_FAILED = "REFRESH_FAILED";
    public static final String SECURITY_AUDIT_EVENT_ACCESS_TOKEN_CREATED = "ACCESS_TOKEN_CREATED";
    public static final String SECURITY_AUDIT_EVENT_REFRESH_TOKEN_CREATED = "REFRESH_TOKEN_CREATED";
    public static final String SECURITY_AUDIT_EVENT_REFRESH_TOKEN_CLEANUP = "REFRESH_TOKEN_CLEANUP";
    
    public static final String LOGGED_OUT_SUCCESSFULLY_MSG ="Logged out successfully.";
    public static final String LOGGED_OUT_ALL_SUCCESSFULLY_MSG ="Logged out from all devices successfully.";
    
    public static final String SESSION_STATUS_ACTIVE = "ACTIVE";
    public static final String SESSION_STATUS_TERMINATED = "TERMINATED";
    
    public static final String SECURITY_AUDIT_EVENT_SESSION_CREATED = "SESSION_CREATED";
    public static final String SECURITY_AUDIT_EVENT_SESSION_TERMINATED = "SESSION_TERMINATED";
    public static final String SECURITY_AUDIT_EVENT_SESSION_TERMINATED_ALL = "SESSION_TERMINATED_ALL";
    public static final String SECURITY_AUDIT_EVENT_SESSION_VIEWED = "SESSION_VIEWED";

}