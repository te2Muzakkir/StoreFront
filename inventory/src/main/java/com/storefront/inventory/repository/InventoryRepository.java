package com.storefront.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.storefront.inventory.entity.Inventory;
import com.storefront.inventory.entity.InventoryId;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {
	
	Optional<Inventory> findByIdProductIdAndIdSellerId(Long productId, Long sellerId);

    @Query("select i from Inventory i where i.id.productId = :productId and i.id.sellerId = :sellerId ")
    Optional<Inventory> findByProductIdAndSellerId(Long productId, Long sellerId);

}