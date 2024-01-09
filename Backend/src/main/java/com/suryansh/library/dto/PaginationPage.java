package com.suryansh.library.dto;

import java.util.List;

public record PaginationPage(
        int page_no,
        int total_pages,
        List<?> items,
        long total_records,
        int page_size) {

}
