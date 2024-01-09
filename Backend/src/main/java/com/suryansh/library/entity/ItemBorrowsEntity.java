package com.suryansh.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_borrows")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ItemBorrowsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private LibraryItemsEntity libraryItem;
    private int quantity;
    private LocalDateTime borrowDate;
    private LocalDate expectedReturnDate;
    private LocalDateTime actualReturnDate;
    private boolean returnStatus;
    private boolean returnedOnTime;
    private String itemCondition;
    @ManyToOne(fetch = FetchType.LAZY)
    private IssuerEntity issuer;
    @ManyToOne(fetch = FetchType.LAZY)
    private FinePdfEntity finePdf;
}
