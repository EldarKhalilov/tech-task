package com.theus.tt.dto;

import java.time.LocalDate;
import java.util.List;

public record NutritionHistoryRecord(
        List<DailySummary> history
) {
    public record DailySummary(
            LocalDate date,
            Double totalCalories,
            Integer mealsCount
    ) {}
}
