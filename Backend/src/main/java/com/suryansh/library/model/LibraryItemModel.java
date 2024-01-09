package com.suryansh.library.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LibraryItemModel {
    @NotNull(message = "Title can't be null")
    @NotBlank(message = "Title can't be blank")
    private String title;
    private String publisher;
    private String subject;
    @NotNull(message = "Item type can't be blank")
    private ItemType itemType;
    private String description;
    @NotBlank(message = "Item location can't be empty")
    private String itemLocation;
    private String itemLanguage;
    @Valid
    private ItemStock itemStock;
    private BookModel bookModel;
    private MagazineModel magazineModel;

    public enum ItemType {
        BOOK,
        MAGAZINE,
        NEWSPAPER,
        OTHER
    }


    @Data
    public static class ItemStock {
        @Min(value = 0, message = "quantity can't be less than 0")
        private int quantity;
    }

    @Data
    public static class BookModel {
        @NotBlank(message = "Book author can't be blank")
        private String author;
        @NotNull(message = "Book publication year can't be null")
        private Integer publicationYear;
        @NotNull(message = "Book type can't be null")
        private String bookType;
        private String bookClass;
    }

    @Data
    public static class MagazineModel {
        @NotBlank(message = "Magazine publish date can't be empty")
        private LocalDate publishedDate;
        @NotBlank(message = "Magazine editor can't be empty")
        private String editor;
    }
}
