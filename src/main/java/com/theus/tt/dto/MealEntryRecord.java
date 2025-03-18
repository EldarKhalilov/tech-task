package com.theus.tt.dto;

import com.theus.tt.entity.MealType;

import java.time.LocalDateTime;
import java.util.List;

public record MealEntryRecord(
        Long userId,
        LocalDateTime mealTime,
        MealType mealType,
        List<MealDishRequest> dishes
) {
    public record MealDishRequest(
            Long dishId,
            Integer portions
    ) {}
}
