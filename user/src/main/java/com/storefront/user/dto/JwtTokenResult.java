package com.storefront.user.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JwtTokenResult {


    private final String accessToken;
    private final Instant expiresAt;

}