package com.suryansh.library.dto;

import java.util.List;

public record LibraryItemPagingDto(
        int page_no,
        int total_pages,
        List<Items> items,
        long total_records,
        int page_size
) {
    public record Items(
            long id, String item_name, String publisher, String itemType, String description, String itemLocation
            , String itemLanguage, String item_unique_id) {
    }
}
