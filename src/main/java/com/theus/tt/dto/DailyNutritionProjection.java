package com.theus.tt.dto;

import java.time.LocalDate;

public record DailyNutritionProjection(
        LocalDate date,
        Double calories,
        Long mealCount
) {
    // Явный конструктор для Hibernate
    public DailyNutritionProjection(
            LocalDate date,
            Number calories,
            Number mealCount
    ) {
        this(
                date,
                calories != null ? calories.doubleValue() : 0.0,
                mealCount != null ? mealCount.longValue() : 0L
        );
    }
}
