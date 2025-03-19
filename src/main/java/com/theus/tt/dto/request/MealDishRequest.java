package com.theus.tt.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MealDishRequest(
        @NotNull Long dishId,
        @Min(1) Integer portions
) {}
