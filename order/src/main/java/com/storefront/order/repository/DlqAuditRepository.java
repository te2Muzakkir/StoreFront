package com.storefront.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storefront.order.entity.DlqAudit;

@Repository
public interface DlqAuditRepository extends JpaRepository<DlqAudit, Long> {

}