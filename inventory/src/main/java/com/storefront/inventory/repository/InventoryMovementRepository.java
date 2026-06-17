package com.storefront.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storefront.inventory.entity.InventoryMovement;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
	
	List<InventoryMovement> findByProductIdAndSellerIdOrderByCreatedAtDesc(Long productId, Long sellerId);
	
	List<InventoryMovement> findByReferenceAndMovementType(String reference, String movementType);

}