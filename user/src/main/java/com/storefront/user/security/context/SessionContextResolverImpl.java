package com.storefront.user.security.context;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class SessionContextResolverImpl implements SessionContextResolver {

	@Override
	public SessionContext resolve(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
        return SessionContext.builder()
                .deviceName(extractDeviceName(userAgent))
                .browser(extractBrowser(userAgent))
                .operatingSystem(extractOperatingSystem(userAgent))
                .ipAddress(extractClientIp(request))
                .userAgent(userAgent)
                .build();
	}
	
	private String extractClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) 
            return forwardedFor.split(",")[0].trim();
        return request.getRemoteAddr();
    }

    private String extractBrowser(String userAgent) {
        if (userAgent == null) 
            return "Unknown";
        if (userAgent.contains("Edg")) 
            return "Microsoft Edge";
        if (userAgent.contains("Chrome")) 
            return "Google Chrome";
        if (userAgent.contains("Firefox")) 
            return "Mozilla Firefox";
        if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) 
            return "Safari";
        return "Unknown";
    }

    private String extractOperatingSystem(String userAgent) {
        if (userAgent == null) 
            return "Unknown";
        if (userAgent.contains("Windows")) 
            return "Windows";
        if (userAgent.contains("Android")) 
            return "Android";
        if (userAgent.contains("iPhone")) 
            return "iOS";
        if (userAgent.contains("Mac")) 
            return "macOS";
        if (userAgent.contains("Linux")) 
            return "Linux";
        return "Unknown";
    }

    private String extractDeviceName(String userAgent) {
        if (userAgent == null) 
            return "Unknown Device";
        if (userAgent.contains("Mobile")) 
            return "Mobile Device";
        return "Desktop";
    }

}
