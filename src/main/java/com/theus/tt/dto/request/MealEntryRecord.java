package com.theus.tt.dto.request;

import com.theus.tt.entity.enums.MealType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record MealEntryRecord(
        @NotNull Long userId,
        @NotNull MealType mealType,
        @NotNull @FutureOrPresent LocalDateTime mealTime,
        @NotEmpty List<@Valid MealDishRequest> dishes
) {}
