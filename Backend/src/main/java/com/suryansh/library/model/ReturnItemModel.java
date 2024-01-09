package com.suryansh.library.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReturnItemModel {
    @NotBlank(message = "Item unique_id can't be blank")
    private String itemUniqueId;
    @Min(value = 0, message = "Quantity can't be less than 0")
    private int quantity;
    @Min(value = 0, message = "Borrow id can't be less than 0")
    private long borrowId;
    @NotBlank(message = "Issuer unique_id can't be blank")
    private String issuerUniqueId;
    @NotNull(message = "Item condition can't be null")
    private ItemCondition itemCondition;

    public enum ItemCondition {
        EXCELLENT,
        GOOD,
        AVERAGE,
        BAD,
        POOR
    }
}
