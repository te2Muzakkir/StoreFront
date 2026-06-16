package com.storefront.order.entity;

import java.time.LocalDateTime;

import com.storefront.order.config.OrderSagaStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class OrderSaga {
	
	@Id
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderSagaStatus status;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Boolean paymentCommandSent = false;

    private Boolean inventoryConfirmSent = false;

    private Boolean inventoryReleaseSent = false;
    
    private Integer recoveryRetryCount = 0;

    private LocalDateTime lastRecoveryAttemptAt;


    public OrderSaga() {}

    public OrderSaga(Long orderId) {
        this.orderId = orderId;
        this.status = OrderSagaStatus.STARTED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void inventoryReserved() {
        this.status = OrderSagaStatus.INVENTORY_RESERVED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void paymentCompleted() {
        this.status = OrderSagaStatus.PAYMENT_COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void waitingInventoryConfirm() {
        this.status = OrderSagaStatus.WAITING_INVENTORY_CONFIRM;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void waitingInventoryRelease() {
        this.status = OrderSagaStatus.WAITING_INVENTORY_RELEASE;
        this.updatedAt = LocalDateTime.now();
    }

    public void completed() {
        this.status = OrderSagaStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void failed() {
        this.status = OrderSagaStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateRetryCount() {
        this.recoveryRetryCount++;
        this.lastRecoveryAttemptAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void manualInterventionRequired() {
        this.status = OrderSagaStatus.MANUAL_INTERVENTION_REQUIRED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean canSendPaymentCommand() {
        return !Boolean.TRUE.equals(paymentCommandSent);
    }

    public boolean canSendInventoryConfirm() {
        return !Boolean.TRUE.equals(inventoryConfirmSent);
    }

    public boolean canSendInventoryRelease() {
        return !Boolean.TRUE.equals(inventoryReleaseSent);
    }

	public Long getOrderId() {
		return orderId;
	}

	public OrderSagaStatus getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setPaymentCommandSent(Boolean paymentCommandSent) {
		this.paymentCommandSent = paymentCommandSent;
	}

	public void setInventoryConfirmSent(Boolean inventoryConfirmSent) {
		this.inventoryConfirmSent = inventoryConfirmSent;
	}

	public void setInventoryReleaseSent(Boolean inventoryReleaseSent) {
		this.inventoryReleaseSent = inventoryReleaseSent;
	}

	public Integer getRecoveryRetryCount() {
		return recoveryRetryCount;
	}

	public void setRecoveryRetryCount(Integer recoveryRetryCount) {
		this.recoveryRetryCount = recoveryRetryCount;
	}

	public LocalDateTime getLastRecoveryAttemptAt() {
		return lastRecoveryAttemptAt;
	}

	public void setLastRecoveryAttemptAt(LocalDateTime lastRecoveryAttemptAt) {
		this.lastRecoveryAttemptAt = lastRecoveryAttemptAt;
	}
    
}