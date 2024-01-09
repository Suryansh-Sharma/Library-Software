package com.suryansh.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BorrowItemDto(
        long borrow_id,
        int quantity,
        String item_name,
        String item_unique_id,
        String issuer_unique_id,
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
        LocalDateTime borrowDate,
        LocalDate expectedReturnDate,
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
        LocalDateTime actualReturnDate,
        boolean returnStatus,
        boolean isReturnPeriodOver,
        String itemCondition
) {
}
