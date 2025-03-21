package com.theus.tt.dto;

import java.time.LocalDate;

public record DailyNutritionProjection(
        LocalDate date,
        Double calories,
        Long mealCount
) {}
