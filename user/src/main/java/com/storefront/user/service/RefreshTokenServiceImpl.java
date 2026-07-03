package com.storefront.user.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.user.config.RefreshTokenProperties;
import com.storefront.user.config.UserConstants;
import com.storefront.user.dto.RefreshTokenDto;
import com.storefront.user.entity.RefreshToken;
import com.storefront.user.entity.User;
import com.storefront.user.exception.ConcurrentRefreshException;
import com.storefront.user.exception.ExpiredRefreshTokenException;
import com.storefront.user.exception.InvalidRefreshTokenException;
import com.storefront.user.exception.RefreshTokenNotFoundException;
import com.storefront.user.exception.RefreshTokenReuseDetectedException;
import com.storefront.user.repository.RefreshTokenRepository;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final SecureRandom secureRandom = new SecureRandom();
	private final RefreshTokenRepository refreshTokenRepository;
	private final RefreshTokenProperties refreshTokenProperties;
	private final SecurityAuditService securityAuditService;
	
	@Value("${refresh-token.cleanup.retention-days}")
    private long retentionDays;

	private String hash(String refreshToken) {
		if (refreshToken == null || refreshToken.isBlank()) 
			throw new IllegalArgumentException("Refresh token cannot be null or blank.");
		try {
			MessageDigest digest = MessageDigest.getInstance(UserConstants.HASH_ALGORITHM);
			byte[] hashBytes = digest.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
			//creating the hash
			StringBuilder builder = new StringBuilder(hashBytes.length * 2);
			for (byte b : hashBytes)
				builder.append(String.format("%02x", b));
			return builder.toString();
		} catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("SHA-256 algorithm is not available.", ex);
		}
	}

	private String generate() {
		byte[] bytes = new byte[64];
		secureRandom.nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}

	private Instant calculateExpiry() {
		return Instant.now().plus(refreshTokenProperties.getValidityDays(), ChronoUnit.DAYS);
	}

	private RefreshToken buildRefreshToken(User user, String rawRefreshToken) {
		Instant now = Instant.now();
		return RefreshToken.builder()
				.tokenId(UUID.randomUUID())
				.hashedToken(hash(rawRefreshToken))
				.user(user)
				.status(UserConstants.TOKEN_STATUS_ACTIVE)
				.createdAt(now)
				.expiresAt(calculateExpiry())
				.lastUsedAt(null)
				.build();
	}

	@Override
	public RefreshTokenDto create(User user) {
		String rawRefreshToken = generate();
		RefreshToken refreshToken = buildRefreshToken(user, rawRefreshToken);
		refreshTokenRepository.save(refreshToken);
		securityAuditService.log(UserConstants.SECURITY_AUDIT_EVENT_REFRESH_TOKEN_CREATED, user, "Refresh token created.");
		return RefreshTokenDto.builder()
				.refreshToken(rawRefreshToken)
				.tokenId(refreshToken.getTokenId())
				.expiresAt(refreshToken.getExpiresAt())
				.build();
	}

	private RefreshToken getRefreshToken(String rawRefreshToken) {
		String hashedToken = hash(rawRefreshToken);
		return refreshTokenRepository.findByHashedToken(hashedToken)
				.orElseThrow(() ->new RefreshTokenNotFoundException("Refresh token not found."));
	}

	private void validateStatus(RefreshToken refreshToken) {
		if (UserConstants.TOKEN_STATUS_REVOKED.equals(refreshToken.getStatus())) 
			throw new RefreshTokenReuseDetectedException("Refresh token reuse detected.", refreshToken.getUser());
		if (UserConstants.TOKEN_STATUS_EXPIRED.equals(refreshToken.getStatus())) 
			throw new ExpiredRefreshTokenException("Refresh token has expired.");
	}

	private void validateExpiry(RefreshToken refreshToken) {
		if (refreshToken.getExpiresAt().isAfter(Instant.now())) 
			return;
		refreshToken.setStatus(UserConstants.TOKEN_STATUS_EXPIRED);
		refreshTokenRepository.save(refreshToken);
		throw new ExpiredRefreshTokenException("Refresh token has expired.");
	}

	@Override
	public RefreshToken validate(String rawRefreshToken) {
		if (rawRefreshToken == null || rawRefreshToken.isBlank()) 
			throw new InvalidRefreshTokenException("Refresh token cannot be null or blank.");
		RefreshToken refreshToken = getRefreshToken(rawRefreshToken);
		validateStatus(refreshToken);
		validateExpiry(refreshToken);
		return refreshToken;
	}

	@Override
	public RefreshTokenDto rotate(String rawRefreshToken) {
		RefreshToken refreshToken = validate(rawRefreshToken);
		try {
			refreshToken.setStatus(UserConstants.TOKEN_STATUS_ROTATED);
			refreshToken.setLastUsedAt(Instant.now());
			refreshTokenRepository.save(refreshToken);
			return create(refreshToken.getUser());
		} catch (ObjectOptimisticLockingFailureException | OptimisticLockException ex) {
			securityAuditService.log(UserConstants.SECURITY_AUDIT_EVENT_REFRESH_FAILED, refreshToken.getUser(),  "Concurrent refresh detected.");
			throw new ConcurrentRefreshException("Concurrent refresh detected.", ex);
		}
	}

	@Override
	public void revoke(String rawRefreshToken) {
		if (rawRefreshToken == null || rawRefreshToken.isBlank()) 
			throw new InvalidRefreshTokenException("Refresh token cannot be null or blank.");
		RefreshToken refreshToken = getRefreshToken(rawRefreshToken);
		if (UserConstants.TOKEN_STATUS_REVOKED.equals(refreshToken.getStatus())) 
			return;
		refreshToken.setStatus(UserConstants.TOKEN_STATUS_REVOKED);
		refreshToken.setLastUsedAt(Instant.now());
		refreshTokenRepository.save(refreshToken);
	}

	@Override
	@Transactional
	public void revokeAll(User user) {
		List<RefreshToken> activeTokens = refreshTokenRepository.findByUserAndStatus(user, UserConstants.TOKEN_STATUS_ACTIVE);
		if (activeTokens.isEmpty()) 
			return;
		Instant now = Instant.now();
		for (RefreshToken token : activeTokens) {
			token.setStatus(UserConstants.TOKEN_STATUS_REVOKED);
			token.setLastUsedAt(now);
		}
		List<RefreshToken> revokedTokens = refreshTokenRepository.saveAll(activeTokens);
		securityAuditService.log(UserConstants.SECURITY_AUDIT_EVENT_LOGOUT_ALL, user, "Revoked " + revokedTokens.size() + " refresh token(s).");
	}

	@Override
	@Transactional(readOnly = true)
	public RefreshToken findByTokenId(UUID tokenId) {
		return refreshTokenRepository.findByTokenId(tokenId)
				.orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found."));
	}
	
	@Override
    public long cleanupExpiredTokens() {
        Instant cutoffTime = Instant.now().minus(retentionDays, ChronoUnit.DAYS);
        long deletedCount = refreshTokenRepository.deleteByStatusAndExpiresAtBefore(UserConstants.TOKEN_STATUS_EXPIRED, cutoffTime);
        securityAuditService.log(UserConstants.SECURITY_AUDIT_EVENT_REFRESH_TOKEN_CLEANUP, null, "Deleted " + deletedCount + " expired refresh tokens.");
        return deletedCount;
    }
	
	@Override
	@Transactional
	public void revokeByTokenId(UUID tokenId) {
	    RefreshToken refreshToken = refreshTokenRepository.findByTokenId(tokenId)
	            .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found."));
		if (UserConstants.TOKEN_STATUS_REVOKED.equals(refreshToken.getStatus())) 
			return;
		refreshToken.setStatus(UserConstants.TOKEN_STATUS_REVOKED);
		refreshToken.setLastUsedAt(Instant.now());
		refreshTokenRepository.save(refreshToken);
	}

}