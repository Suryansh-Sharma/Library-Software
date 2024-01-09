package com.suryansh.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "fine_pdf")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FinePdfEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fileName;
    private LocalDateTime dateTime;
    private double amount;
    private int totalItems;

    @ManyToOne(fetch = FetchType.LAZY)
    private IssuerEntity issuer;

    @OneToMany(targetEntity = ItemBorrowsEntity.class, cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "fine_pdf_id")
    private List<ItemBorrowsEntity> itemBorrows;


}
