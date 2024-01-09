package com.suryansh.library.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ItemFullDetailDto {
    private long id;
    private String title;
    private String publisher;
    private String itemType;
    private String subject;
    private String description;
    private String itemLocation;
    private String itemLanguage;
    private String uniqueId;
    private StockDto stock;
    private BookDto book;
    private MagazineDto magazine;

    @Data
    public static class BookDto {
        private int id;
        private String author;
        private Integer publicationYear;
        private String bookType;
        private String bookClass;
    }

    @Data
    public static class MagazineDto {
        private long id;
        private LocalDate publishedDate;
        private String editor;
    }

    @Data
    public static class StockDto {
        private long id;
        private int quantity;
        private int availableQuantity;
        private LocalDateTime lastStocked;
        private LocalDateTime lastCheckedOut;
        private LocalDateTime lastCheckedIn;
    }
}
