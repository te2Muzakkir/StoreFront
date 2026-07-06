package com.storefront.user.filter;

import static com.storefront.user.config.UserConstants.CORRELATION_ID_ATTRIBUTE;
import static com.storefront.user.config.UserConstants.CORRELATION_ID_HEADER;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String correlationId = request.getHeader(CORRELATION_ID_HEADER);
            if (correlationId == null || correlationId.isBlank()) 
            	response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing X-Correlation-ID header");
            MDC.put(CORRELATION_ID_ATTRIBUTE, correlationId);
            filterChain.doFilter(request, response);
        } finally {
        	MDC.remove(CORRELATION_ID_ATTRIBUTE);
        }
    }

}