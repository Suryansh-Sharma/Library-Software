package com.suryansh.library.dto;

import lombok.Data;

@Data
public class ChartQueryDto {
    public ChartQueryDto(Long totalRecords, String target) {
        this.total_records = totalRecords;
        this.target = target;
    }

    private Long total_records;
    private String target;

}
