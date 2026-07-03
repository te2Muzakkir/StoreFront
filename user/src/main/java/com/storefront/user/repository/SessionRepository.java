package com.storefront.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storefront.user.entity.Session;
import com.storefront.user.entity.User;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findBySessionId(String sessionId);
    Optional<Session> findByRefreshTokenId(String refreshTokenId);
    List<Session> findByUserAndStatus(User user, String status);
    long countByUserAndStatus(User user, String status);

}