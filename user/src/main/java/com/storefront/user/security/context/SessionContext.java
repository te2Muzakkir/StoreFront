package com.storefront.user.security.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionContext {

    private String deviceName;

    private String browser;

    private String operatingSystem;

    private String ipAddress;

    private String userAgent;

}