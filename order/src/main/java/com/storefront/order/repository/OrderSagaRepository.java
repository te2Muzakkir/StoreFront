package com.storefront.order.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storefront.order.config.OrderSagaStatus;
import com.storefront.order.entity.OrderSaga;

@Repository
public interface OrderSagaRepository extends JpaRepository<OrderSaga, Long> {
	
	List<OrderSaga> findByStatusInAndUpdatedAtBefore(Collection<OrderSagaStatus> statuses, LocalDateTime updatedAt);

}