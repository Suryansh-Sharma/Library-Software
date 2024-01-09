package com.suryansh.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.LocalDateTime;

public record FineInfoDto(long id,
                          @JsonDeserialize(using = LocalDateDeserializer.class)
                          @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
                          LocalDateTime payed_on,
                          double total_amount,
                          String download_slip,
                          int total_items,
                          IssuerInfo issuer_info) {
    public record IssuerInfo(long id,
                             String first_name,
                             String last_name,
                             int roll_no,
                             String issuer_type,
                             String contact_no,
                             String email,
                             String issuer_class,
                             String unique_id,
                             String issuer_branch) {
    }
}
