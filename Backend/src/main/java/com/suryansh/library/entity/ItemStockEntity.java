package com.suryansh.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "item_stock")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ItemStockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int quantity;
    private int availableQuantity;
    private LocalDateTime lastStocked;
    private LocalDateTime lastCheckedOut;
    private LocalDateTime lastCheckedIn;
}
