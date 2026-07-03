package com.storefront.user.security.context;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public interface SessionContextResolver {
	
	SessionContext resolve(HttpServletRequest request);

}