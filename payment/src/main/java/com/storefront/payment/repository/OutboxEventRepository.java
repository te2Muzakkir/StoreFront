package com.storefront.payment.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.storefront.payment.entity.OutboxEvent;

import jakarta.persistence.LockModeType;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, String> {
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select o from OutboxEvent o where o.status = :status order by o.createdAt")
	List<OutboxEvent> findTop100ForUpdate(@Param("status") String status, Pageable pageable);
	
	@Query("select o from OutboxEvent o where o.status = 'PROCESSING' and o.processingStartedAt < :cutoff")
	List<OutboxEvent> findStuckProcessingEvents(@Param("cutoff") LocalDateTime cutoff);
	
	@Modifying
	@Query(" delete from OutboxEvent o where o.status = :status and o.publishedAt < :cutoff ")
	int deletePublishedEvents(@Param("status") String status, @Param("cutoff") LocalDateTime cutoff);
	
	long countByStatus(String status);
	
	List<OutboxEvent> findByStatus(String status);

}