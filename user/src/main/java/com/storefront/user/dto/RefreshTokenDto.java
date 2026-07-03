package com.storefront.user.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RefreshTokenDto {
	
	private String refreshToken;
    private UUID tokenId;
    private Instant expiresAt;

}