package com.storefront.user.service;

import org.springframework.stereotype.Service;

import com.storefront.user.entity.User;

@Service
public interface SecurityAuditService {
	
	public void log(String event, User user, String message);

}