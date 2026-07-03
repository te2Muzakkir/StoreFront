package com.storefront.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storefront.user.dto.SessionsResponse;
import com.storefront.user.service.SessionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    
    @GetMapping
    public ResponseEntity<SessionsResponse> getSessions() {
        return ResponseEntity.ok( SessionsResponse.builder()
                        .sessions(sessionService.getCurrentUserSessions()).build());
    }
    
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<String> terminateSession(@PathVariable String sessionId) {
        sessionService.terminateSession(sessionId);
        return ResponseEntity.ok().body("Session terminated successfully.");
    }
    
    @DeleteMapping
    public ResponseEntity<String> terminateAllSessions() {
        sessionService.terminateAllSessionsExceptCurrent();
        return ResponseEntity.ok().body("All other sessions terminated successfully.");
    }

}