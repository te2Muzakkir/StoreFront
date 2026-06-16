package com.storefront.inventory.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Inventory {

	@EmbeddedId
	private InventoryId id;
	private int quantity;
	private LocalDateTime updatedAt;
    @Column(nullable = false)
    private int reservedQuantity;
    @Version
    private long version; // Optimistic locking (mandatory)

}