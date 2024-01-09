package com.suryansh.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "issuers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IssuerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstname;
    private String lastname;
    private int rollNo;
    private String issuerType;
    private String contactNo;
    private String email;
    private String issuerClass;
    private String uniqueId;
    private String issuerBranch;
    @OneToMany(targetEntity = ItemBorrowsEntity.class, cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "issuer_id")
    private List<ItemBorrowsEntity> itemBorrows;

    @JoinColumn(name = "issuer_id")
    @OneToMany(targetEntity = FinePdfEntity.class, cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FinePdfEntity> userFinesPdf;
}
