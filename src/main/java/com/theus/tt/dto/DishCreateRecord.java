package com.theus.tt.dto;

public record DishCreateRecord(
        String name,
        Double caloriesPerPortion,
        Double proteins,
        Double fats,
        Double carbs
) {}
