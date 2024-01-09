package com.suryansh.library.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowItemModel {
    @NotBlank(message = "Item unique_id can't be blank")
    private String itemUniqueId;
    @Min(value = 0, message = "Quantity can't be less than 0")
    private int quantity;
    private LocalDate expectedReturnDate;
    @NotBlank(message = "Issuer unique_id can't be blank")
    private String issuerUniqueId;
}
