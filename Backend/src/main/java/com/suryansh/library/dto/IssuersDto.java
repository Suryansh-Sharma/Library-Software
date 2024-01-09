package com.suryansh.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.LocalDateTime;
import java.util.List;

public record IssuersDto(
        long id,
        String first_name,
        String last_name,
        int roll_no,
        String issuer_type,
        String contact_no,
        String email,
        String issuer_class,
        String unique_id,
        String issuer_branch,
        List<Fine> allFine
) {
    public record Fine(
            long id,
            @JsonDeserialize(using = LocalDateDeserializer.class)
            @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
            LocalDateTime payDateTime,
            int totalItems,
            String fileName,
            String downloadUrl,
            double amount,
            List<BorrowItemDto> items) {
    }
}
