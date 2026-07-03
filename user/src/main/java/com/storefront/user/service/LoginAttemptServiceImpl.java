package com.storefront.user.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.user.entity.LoginAttempt;
import com.storefront.user.repository.LoginAttemptRepository;
import com.storefront.user.security.RequestContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginAttemptServiceImpl implements LoginAttemptService {
	
	private final LoginAttemptRepository loginAttemptRepository;

	@Override
	public void recordSuccessfulLogin(Long userId, String emailAttempted, RequestContext context) {
		LoginAttempt attempt = LoginAttempt.builder()
                .userId(userId)
                .emailAttempted(emailAttempted)
                .successful(true)
                .failureReason(null)
                .attemptTime(LocalDateTime.now())
                .ipAddress(context.getIpAddress())
                .userAgent(context.getUserAgent())
                .correlationId(context.getCorrelationId())
                .build();
        loginAttemptRepository.save(attempt);
	}

	@Override
	public void recordFailedLogin(Long userId, String emailAttempted, RequestContext context, String reason) {
		LoginAttempt attempt = LoginAttempt.builder()
                .userId(userId)
                .emailAttempted(emailAttempted)
                .successful(false)
                .failureReason(reason)
                .attemptTime(LocalDateTime.now())
                .ipAddress(context.getIpAddress())
                .userAgent(context.getUserAgent())
                .correlationId(context.getCorrelationId())
                .build();
        loginAttemptRepository.save(attempt);

	}

}