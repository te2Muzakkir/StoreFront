package com.storefront.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storefront.payment.entity.PaymentDlq;

@Repository
public interface PaymentDlqRepository extends JpaRepository<PaymentDlq, Long> {

}