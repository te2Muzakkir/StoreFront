package com.storefront.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.storefront.user.dto.RefreshTokenDto;
import com.storefront.user.entity.RefreshToken;
import com.storefront.user.entity.User;

@Service
public interface RefreshTokenService {
	
	public RefreshTokenDto create(User user);

	public RefreshToken validate(String refreshToken);

	public RefreshTokenDto rotate(String refreshToken);

	public void revoke(String refreshToken);

	public void revokeAll(User user);
	
	public RefreshToken findByTokenId(UUID tokenId);
	
	public long cleanupExpiredTokens();
	
	void revokeByTokenId(UUID tokenId);

}