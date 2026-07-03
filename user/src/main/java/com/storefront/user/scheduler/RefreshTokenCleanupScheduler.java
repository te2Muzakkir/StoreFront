package com.storefront.user.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.storefront.user.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupScheduler {

	private final RefreshTokenService refreshTokenService;

	@Value("${refresh-token.cleanup.enabled:true}")
	private boolean cleanupEnabled;

	@Scheduled(cron = "${refresh-token.cleanup.cron}")
	public void cleanupExpiredRefreshTokens() {
		if (!cleanupEnabled) {
			log.info("Refresh token cleanup scheduler is disabled.");
			return;
		}
		long start = System.currentTimeMillis();
		try {
			log.info("Starting refresh token cleanup.");
			long deleted = refreshTokenService.cleanupExpiredTokens();
			log.info("Refresh token cleanup completed. Deleted {} tokens in {} ms.", deleted, System.currentTimeMillis() - start);
		} catch (RuntimeException ex) {
			log.error("Refresh token cleanup failed after {} ms.", System.currentTimeMillis() - start, ex);
		}
	}

}