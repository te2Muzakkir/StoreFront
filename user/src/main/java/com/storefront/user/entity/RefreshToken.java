package com.storefront.user.entity;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "refresh_token",
    indexes = {
        @Index(name = "idx_refresh_token_hash", columnList = "hashed_token", unique = true),
        @Index(name = "idx_refresh_token_user", columnList = "user_id"),
        @Index(name = "idx_refresh_token_expiry", columnList = "expires_at"),
        @Index(name = "idx_refresh_token_status", columnList = "status"),
        @Index(name = "idx_refresh_token_token_id", columnList = "token_id", unique = true)
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class RefreshToken {

    @Id
    @GeneratedValue(generator="refresh_token_seq")
	@SequenceGenerator(name="refresh_token_seq",sequenceName="sf_refresh_token_seq", allocationSize=1)
    private Long id;
    @Column(name = "token_id", nullable = false, unique = true, updatable = false)
    private UUID tokenId;
    @Column(name = "hashed_token", nullable = false, unique = true, length = 64)
    private String hashedToken;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false, length = 20)
    private String status;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;
    @Column(name = "last_used_at")
    private Instant lastUsedAt;
    @Version
    private Long version;

}