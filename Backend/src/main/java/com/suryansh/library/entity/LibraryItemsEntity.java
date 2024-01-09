package com.suryansh.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "library_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LibraryItemsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String publisher;
    private String subject;
    private String itemType;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String itemLocation;
    private String itemLanguage;
    private String uniqueId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private BooksEntity books;

    @OneToOne(cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "magazine_id")
    private MagazineEntity magazines;

    @OneToOne(targetEntity = ItemStockEntity.class, cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_stock_id")
    private ItemStockEntity itemStock;
    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "library_item_id")
    private List<ItemBorrowsEntity> borrows;

    @Override
    public String toString() {
        return "LibraryItemsEntity{" +
                "title='" + title + '\'' +
                '}';
    }
}
