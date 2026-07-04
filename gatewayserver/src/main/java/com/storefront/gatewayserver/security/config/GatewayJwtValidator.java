package com.storefront.gatewayserver.security.config;

import static com.storefront.gatewayserver.config.GatewayConstants.JWT_VALIDATOR_ACCESS_TOKEN;
import static com.storefront.gatewayserver.config.GatewayConstants.JWT_VALIDATOR_CLAIM_ROLES;
import static com.storefront.gatewayserver.config.GatewayConstants.JWT_VALIDATOR_CLAIM_TYPE;
import static com.storefront.gatewayserver.config.GatewayConstants.JWT_VALIDATOR_CLAIM_UID;
import static com.storefront.gatewayserver.config.GatewayConstants.JWT_VALIDATOR_INVALID_ROLE_VALUE_MSG;
import static com.storefront.gatewayserver.config.GatewayConstants.JWT_VALIDATOR_INVALID_TOKEN;
import static com.storefront.gatewayserver.config.GatewayConstants.JWT_VALIDATOR_INVALID_TOKEN_ROLES_EMPTY_MSG;
import static com.storefront.gatewayserver.config.GatewayConstants.JWT_VALIDATOR_INVALID_TOKEN_ROLES_MSG;
import static com.storefront.gatewayserver.config.GatewayConstants.JWT_VALIDATOR_INVALID_TOKEN_SUBJECT_MSG;
import static com.storefront.gatewayserver.config.GatewayConstants.JWT_VALIDATOR_INVALID_TOKEN_TYPE_MSG;
import static com.storefront.gatewayserver.config.GatewayConstants.JWT_VALIDATOR_INVALID_TOKEN_UID_MSG;
import static com.storefront.gatewayserver.config.GatewayConstants.JWT_VALIDATOR_INVALID_TOKEN_USER_ID_MSG;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.storefront.gatewayserver.metrics.GatewayMetricsService;

@Component
public class GatewayJwtValidator implements OAuth2TokenValidator<Jwt> {
	
	@Autowired
	private GatewayMetricsService  gatewayMetricsService;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        OAuth2TokenValidatorResult result;
        
        result = validateSubject(jwt);
        if (result.hasErrors()) {
        	gatewayMetricsService.jwtFailure();
            return result;
        }

        result = validateUserId(jwt);
        if (result.hasErrors()) {
        	gatewayMetricsService.jwtFailure();
            return result;
        }

        result = validateRoles(jwt);
        if (result.hasErrors()) {
        	gatewayMetricsService.jwtFailure();
            return result;
        }

        result = validateTokenType(jwt);
        if (result.hasErrors()) {
        	gatewayMetricsService.jwtFailure();
            return result;
        }

        gatewayMetricsService.jwtSuccess();
        return OAuth2TokenValidatorResult.success();
    }

    private OAuth2TokenValidatorResult validateSubject(Jwt jwt) {
        String subject = jwt.getSubject();
        if (subject == null || subject.isBlank()) 
            return failure(JWT_VALIDATOR_INVALID_TOKEN, JWT_VALIDATOR_INVALID_TOKEN_SUBJECT_MSG);
        return OAuth2TokenValidatorResult.success();
    }

    private OAuth2TokenValidatorResult validateUserId(Jwt jwt) {
        Object uid = jwt.getClaim(JWT_VALIDATOR_CLAIM_UID);
        if (!(uid instanceof Number)) 
            return failure(JWT_VALIDATOR_INVALID_TOKEN, JWT_VALIDATOR_INVALID_TOKEN_UID_MSG);
        long userId = ((Number) uid).longValue();
        if (userId <= 0) 
            return failure(JWT_VALIDATOR_INVALID_TOKEN, JWT_VALIDATOR_INVALID_TOKEN_USER_ID_MSG);
        return OAuth2TokenValidatorResult.success();
    }

    private OAuth2TokenValidatorResult validateRoles(Jwt jwt) {
        Object claim = jwt.getClaim(JWT_VALIDATOR_CLAIM_ROLES);
        if (!(claim instanceof Collection<?> roles)) 
            return failure(JWT_VALIDATOR_INVALID_TOKEN, JWT_VALIDATOR_INVALID_TOKEN_ROLES_MSG);
        if (roles.isEmpty()) 
            return failure(JWT_VALIDATOR_INVALID_TOKEN, JWT_VALIDATOR_INVALID_TOKEN_ROLES_EMPTY_MSG);
        for (Object role : roles) {
            if (!(role instanceof String value) || value.isBlank()) 
                return failure(JWT_VALIDATOR_INVALID_TOKEN, JWT_VALIDATOR_INVALID_ROLE_VALUE_MSG);
        }
        return OAuth2TokenValidatorResult.success();
    }

    private OAuth2TokenValidatorResult validateTokenType(Jwt jwt) {
        String type = jwt.getClaimAsString(JWT_VALIDATOR_CLAIM_TYPE);
        if (!JWT_VALIDATOR_ACCESS_TOKEN.equals(type)) 
            return failure(JWT_VALIDATOR_INVALID_TOKEN, JWT_VALIDATOR_INVALID_TOKEN_TYPE_MSG);
        return OAuth2TokenValidatorResult.success();
    }

    private OAuth2TokenValidatorResult failure(String code, String description) {
        return OAuth2TokenValidatorResult.failure(new OAuth2Error(code, description, null));
    }

}