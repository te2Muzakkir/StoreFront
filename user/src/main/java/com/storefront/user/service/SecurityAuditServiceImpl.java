package com.storefront.user.service;

import org.springframework.stereotype.Service;

import com.storefront.user.entity.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SecurityAuditServiceImpl implements SecurityAuditService {

	@Override
	public void log(String event, User user, String message) {
		log.info("[SECURITY] event={} userId={} email={} message={}",
                event, user != null ? user.getId() : null, user != null ? user.getEmail() : null, message);

	}

}
