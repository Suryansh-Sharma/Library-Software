package com.suryansh.library.dto;

import java.util.List;

public record ChartsDataDto(String labelName,
                            String seriesName,
                            List<Integer>series,
                            List<String>labels,
                            String query) {
}
