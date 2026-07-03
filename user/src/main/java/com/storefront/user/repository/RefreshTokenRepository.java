package com.storefront.user.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.storefront.user.entity.RefreshToken;
import com.storefront.user.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByHashedToken(String hashedToken);
	
	Optional<RefreshToken> findByTokenId(UUID tokenId);

    List<RefreshToken> findByUser(User user);
    
    List<RefreshToken> findByUserAndStatus(User user, String status);
    
    Optional<RefreshToken> findByHashedTokenAndStatus(String hashedToken, String status);

    void deleteByUser(User user);

    long deleteByExpiresAtBefore(Instant time);
    
    long deleteByStatusAndExpiresAtBefore( String status, Instant expiryDate);
    
    long countByStatus(String status);
    
    long countByStatusAndExpiresAtBefore(String status, Instant expiryDate);
    
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.status = :status AND rt.expiresAt < :expiryDate ")
    int deleteExpiredTokens(@Param("status") String status, @Param("expiryDate") Instant expiryDate);

}