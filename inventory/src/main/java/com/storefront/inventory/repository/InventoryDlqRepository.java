package com.storefront.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storefront.inventory.entity.InventoryDlq;

@Repository
public interface InventoryDlqRepository extends JpaRepository<InventoryDlq, Long> {

}