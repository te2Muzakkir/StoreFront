package com.storefront.user.service;

import org.springframework.stereotype.Service;

import com.storefront.user.security.RequestContext;

@Service
public interface LoginAttemptService {
	
	void recordSuccessfulLogin(Long userId, String emailAttempted, RequestContext context);

    void recordFailedLogin(Long userId, String emailAttempted, RequestContext context, String reason);

}