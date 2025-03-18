package com.theus.tt.dto;

import com.theus.tt.entity.MealType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DailyReportRecord(
        LocalDate date,
        Double totalCalories,
        Double dailyNorm,
        Boolean isWithinLimit,
        List<MealInfo> meals
) {
    public record MealInfo(
            MealType type,
            LocalDateTime time,
            Double calories,
            List<DishInfo> dishes
    ) {}

    public record DishInfo(
            String name,
            Integer portions,
            Double calories
    ) {}
}
