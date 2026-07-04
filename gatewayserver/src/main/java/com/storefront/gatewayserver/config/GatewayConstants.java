package com.storefront.gatewayserver.config;

public class GatewayConstants {

	private GatewayConstants() {}

	public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
	public static final String CORRELATION_ID_ATTRIBUTE = "correlationId";
	public static final String REQUEST_START_TIME_ATTRIBUTE = "requestStartTime";

	public static final String JWT_VALIDATOR_INVALID_TOKEN = "invalid_token";
	public static final String JWT_VALIDATOR_CLAIM_UID = "uid";
	public static final String JWT_VALIDATOR_CLAIM_ROLES = "roles";
	public static final String JWT_VALIDATOR_CLAIM_TYPE = "type";
	public static final String JWT_VALIDATOR_ACCESS_TOKEN = "ACCESS";
	public static final String JWT_VALIDATOR_INVALID_TOKEN_SUBJECT_MSG = "Missing subject claim.";
	public static final String JWT_VALIDATOR_INVALID_TOKEN_UID_MSG = "Invalid uid claim.";
	public static final String JWT_VALIDATOR_INVALID_TOKEN_USER_ID_MSG = "Invalid user id.";
	public static final String JWT_VALIDATOR_INVALID_TOKEN_ROLES_MSG = "Missing roles claim.";
	public static final String JWT_VALIDATOR_INVALID_TOKEN_ROLES_EMPTY_MSG = "Roles cannot be empty.";
	public static final String JWT_VALIDATOR_INVALID_ROLE_VALUE_MSG = "Invalid role value.";
	public static final String JWT_VALIDATOR_INVALID_TOKEN_TYPE_MSG = "Invalid token type.";
	
	public static final String[] WHITELISTED_URL = {
			"/storefront/user/api/auth/**",
			"/actuator/**",
			"/swagger-ui/**",
		    "/swagger-ui.html",
		    "/v3/api-docs/**",
		    "/storefront/*/v3/api-docs/**",
		    "/actuator/health",
		    "/actuator/health/**",
		    "/actuator/info"
	    };
	public static final String USER_ROLE_CUSTOMER = "CUSTOMER";
	public static final String USER_ROLE_ADMIN = "ADMIN";
	
	public static final String UNAUTHORIZED_MSG = "Invalid or expired access token.";

    public static final String FORBIDDEN_MSG = "You are not authorized to access this resource.";

}