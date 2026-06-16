package com.storefront.inventory.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class InventoryMovement {

	@Id
	@GeneratedValue(generator = "inventory_movement_seq")
	@SequenceGenerator(name = "inventory_movement_seq", sequenceName = "inventory_movement_id_seq", allocationSize = 1)
	private Long id;
	private Long productId;
	private Long sellerId;
	private int quantityChange;
	private String movementType; // IN / OUT
	private String reference; // "Order - ID"/ "Product - ID"
	private String eventId;
	private LocalDateTime createdAt;

}