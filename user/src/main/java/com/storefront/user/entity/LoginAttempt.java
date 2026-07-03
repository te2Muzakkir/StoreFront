package com.storefront.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "login_attempt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LoginAttempt {

    @Id
    @GeneratedValue(generator = "login_attempt_seq")
    @SequenceGenerator(name = "login_attempt_seq", sequenceName = "sf_login_attempt_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email_attempted", nullable = false, length = 255)
    private String emailAttempted;

    @Column(name = "attempt_time", nullable = false)
    private LocalDateTime attemptTime;

    @Column(nullable = false)
    private boolean successful;

    @Column(name = "failure_reason", length = 50)
    private String failureReason;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;
    
    @Column(name = "correlation_id", length = 100)
    private String correlationId;
    
}