package com.storefront.user.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {

    private String sessionId;
    private String deviceName;
    private String browser;
    private String operatingSystem;
    private String ipAddress;
    private Instant loginAt;
    private Instant lastActivityAt;
    private String status;
    private boolean currentSession;

}