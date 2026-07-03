package com.storefront.user.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.user.config.UserConstants;
import com.storefront.user.dto.SessionResponse;
import com.storefront.user.entity.Session;
import com.storefront.user.entity.User;
import com.storefront.user.exception.SessionAccessDeniedException;
import com.storefront.user.exception.SessionNotFoundException;
import com.storefront.user.repository.SessionRepository;
import com.storefront.user.repository.UserRepository;
import com.storefront.user.security.context.SessionContext;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SessionServiceImpl implements SessionService {
	
	private final SessionRepository sessionRepository;
	private final UserRepository userRepository;
	private final RefreshTokenService refreshTokenService;
	private final SecurityAuditService securityAuditService;
	
	@Override
	@Transactional
	public Session createSession(User user, String refreshTokenId, SessionContext sessionContext) {
	    Instant now = Instant.now();
	    Session session = Session.builder()
	            .sessionId(UUID.randomUUID().toString())
	            .user(user)
	            .refreshTokenId(refreshTokenId)
	            .deviceName(sessionContext.getDeviceName())
	            .browser(sessionContext.getBrowser())
	            .operatingSystem(sessionContext.getOperatingSystem())
	            .ipAddress(sessionContext.getIpAddress())
	            .userAgent(sessionContext.getUserAgent())
	            .loginAt(now)
	            .lastActivityAt(now)
	            .status(UserConstants.SESSION_STATUS_ACTIVE)
	            .build();
	    securityAuditService.log(UserConstants.SECURITY_AUDIT_EVENT_SESSION_CREATED,  user,
	            "Session created. Session ID: " + session.getSessionId());
	    return sessionRepository.save(session);
	}
	
	@Override
	@Transactional
	public void updateLastActivity(String refreshTokenId) {
	    Session session = sessionRepository.findByRefreshTokenId(refreshTokenId)
	            .orElseThrow(() -> new SessionNotFoundException("Session not found."));
	    session.setLastActivityAt(Instant.now());
	    sessionRepository.save(session);
	}
	
	@Override
	@Transactional
	public void terminateSession(String sessionId) {
		User user = getAuthenticatedUser();
	    Session session = sessionRepository.findBySessionId(sessionId)
	            .orElseThrow(() -> new SessionNotFoundException("Session not found."));
	    if (!session.getUser().getId().equals(user.getId())) 
	        throw new SessionAccessDeniedException("Access denied.");
	    if (UserConstants.SESSION_STATUS_TERMINATED.equals(session.getStatus())) 
	        return;
	    refreshTokenService.revokeByTokenId(UUID.fromString((session.getRefreshTokenId())));
	    session.setStatus(UserConstants.SESSION_STATUS_TERMINATED);
	    session.setTerminatedAt(Instant.now());
	    securityAuditService.log(UserConstants.SECURITY_AUDIT_EVENT_SESSION_TERMINATED,
	            user, "Session terminated. Session ID: " + session.getSessionId());
	    sessionRepository.save(session);
	}
	
	@Override
	@Transactional
	public long terminateAllSessions(User user) {
	    List<Session> sessions = sessionRepository.findByUserAndStatus(user, UserConstants.SESSION_STATUS_ACTIVE);
	    Instant now = Instant.now();
	    for (Session session : sessions) {
	        session.setStatus(UserConstants.SESSION_STATUS_TERMINATED);
	        session.setTerminatedAt(now);
	    }
	    sessionRepository.saveAll(sessions);
	    securityAuditService.log(UserConstants.SECURITY_AUDIT_EVENT_SESSION_TERMINATED_ALL,
	            user, "Terminated " + sessions.size() + " session(s).");
	    return sessions.size();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Session> getActiveSessions(User user) {
	    return sessionRepository.findByUserAndStatus(user, UserConstants.SESSION_STATUS_ACTIVE);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Session> findBySessionId(String sessionId) {
	    return sessionRepository.findBySessionId(sessionId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SessionResponse> getCurrentUserSessions() {
	    User user = getAuthenticatedUser();
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentRefreshTokenId = (String) authentication.getDetails();
	    securityAuditService.log(UserConstants.SECURITY_AUDIT_EVENT_SESSION_VIEWED, user, "Viewed active sessions.");
	    return sessionRepository.findByUserAndStatus(user, UserConstants.SESSION_STATUS_ACTIVE)
	            .stream()
	            .map(session -> SessionResponse.builder()
	                    .sessionId(session.getSessionId())
	                    .deviceName(session.getDeviceName())
	                    .browser(session.getBrowser())
	                    .operatingSystem(session.getOperatingSystem())
	                    .ipAddress(session.getIpAddress())
	                    .loginAt(session.getLoginAt())
	                    .lastActivityAt(session.getLastActivityAt())
	                    .status(session.getStatus())
	                    .currentSession(
	                            session.getRefreshTokenId()
	                                    .equals(currentRefreshTokenId))
	                    .build())
	            .toList();
	}

	@Override
	@Transactional
	public long terminateAllSessionsExceptCurrent() {
	    User user = getAuthenticatedUser();
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentRefreshTokenId =  (String) authentication.getDetails();
	    List<Session> sessions = sessionRepository.findByUserAndStatus(user, UserConstants.SESSION_STATUS_ACTIVE);
	    Instant now = Instant.now();
	    long terminated = 0;
	    for (Session session : sessions) {
	        if (session.getRefreshTokenId().equals(currentRefreshTokenId))
	            continue;
	        refreshTokenService.revokeByTokenId(UUID.fromString((session.getRefreshTokenId())));
	        session.setStatus(UserConstants.SESSION_STATUS_TERMINATED);
	        session.setTerminatedAt(now);
	        terminated++;
	    }
	    sessionRepository.saveAll(sessions);
	    return terminated;
	}
	
	private User getAuthenticatedUser() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || !authentication.isAuthenticated()) 
	        throw new InsufficientAuthenticationException("User is not authenticated.");
	    return userRepository.findByEmail(authentication.getName())
	            .orElseThrow(() -> new UsernameNotFoundException("User not found."));
	}

}