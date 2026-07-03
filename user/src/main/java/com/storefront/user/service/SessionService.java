package com.storefront.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.storefront.user.dto.SessionResponse;
import com.storefront.user.entity.Session;
import com.storefront.user.entity.User;
import com.storefront.user.security.context.SessionContext;

@Service
public interface SessionService {

    Session createSession(User user, String refreshTokenId, SessionContext sessionContext);
    void updateLastActivity(String refreshTokenId);
    void terminateSession(String sessionId);
    long terminateAllSessions(User user);
    List<Session> getActiveSessions(User user);
    Optional<Session> findBySessionId(String sessionId);
    List<SessionResponse> getCurrentUserSessions();
    long terminateAllSessionsExceptCurrent();

}