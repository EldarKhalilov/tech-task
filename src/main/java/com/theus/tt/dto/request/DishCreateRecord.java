package com.theus.tt.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record DishCreateRecord(
        @NotBlank(message = "Название обязательно")
        String name,

        @Min(value = 1, message = "Калории не могут быть отрицательными")
        short caloriesPerPortion,

        @Min(1) double proteins,
        @Min(1) double fats,
        @Min(1) double carbohydrates
) {}
