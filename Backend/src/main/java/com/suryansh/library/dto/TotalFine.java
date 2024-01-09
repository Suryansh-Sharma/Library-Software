package com.suryansh.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TotalFine(int total_items, double total_amount, int per_day_amount,
                        @JsonDeserialize(using = LocalDateDeserializer.class)
                        @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
                        LocalDateTime today_date_time,
                        List<FineItem> fines, IssuerInfo issuerInfo) {

    public record IssuerInfo(String name, String issuer_type, String issuer_class, String contact) {
    }
    public record FineItem(String item_unique_id,
                           String item_title,
                           String issuer_unique_id,
                           String issuer_name,
                           long borrow_id,
                           @JsonDeserialize(using = LocalDateDeserializer.class)
                            @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
                            LocalDateTime borrow_date,
                           LocalDate expected_return_date,
                           long pending_days,
                           double per_day_amount,
                           double total_amount){}
}
