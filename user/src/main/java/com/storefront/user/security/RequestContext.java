package com.storefront.user.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestContext {

    private String ipAddress;
    private String userAgent;
    private String correlationId;

}