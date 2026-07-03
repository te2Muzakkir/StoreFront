package com.storefront.user.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
@Table(
    name = "user_session",
    indexes = {
        @Index(name = "idx_session_session_id", columnList = "session_id", unique = true),
        @Index(name = "idx_session_user", columnList = "user_id"),
        @Index(name = "idx_session_status", columnList = "status"),
        @Index(name = "idx_session_last_activity", columnList = "last_activity_at"),
        @Index(name = "idx_session_refresh_token", columnList = "refresh_token_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

    @Id
    @GeneratedValue(generator="session_seq")
	@SequenceGenerator(name="session_seq",sequenceName="sf_session_seq", allocationSize=1)
    private Long id;
    @Column(name = "session_id", nullable = false, unique = true, length = 36)
    private String sessionId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "refresh_token_id", nullable = false, unique = true, length = 36)
    private String refreshTokenId;
    @Column(name = "device_name", length = 100)
    private String deviceName;
    @Column(name = "browser", length = 100)
    private String browser;
    @Column(name = "operating_system", length = 100)
    private String operatingSystem;
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    @Column(name = "user_agent", length = 512)
    private String userAgent;
    @Column(name = "login_at", nullable = false)
    private Instant loginAt;
    @Column(name = "last_activity_at", nullable = false)
    private Instant lastActivityAt;
    @Column(name = "terminated_at")
    private Instant terminatedAt;
    @Column(nullable = false, length = 20)
    private String status;
    @Version
    private Long version;
}