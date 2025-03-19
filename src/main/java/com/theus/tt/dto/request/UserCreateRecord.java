package com.theus.tt.dto.request;

import com.theus.tt.entity.enums.GenderEnum;
import com.theus.tt.entity.enums.GoalEnum;
import jakarta.validation.constraints.*;

public record UserCreateRecord(
        @NotBlank(message = "Name is required")
        String name,

        @Email(message = "Invalid email format")
        String email,

        @Min(value = 9, message = "Age must be at least 9")
        @Max(value = 120, message = "Age cannot exceed 120")
        byte age,

        @Min(value = 70, message = "Weight must be at least 70 kg")
        @Max(value = 220, message = "Weight cannot exceed 220 kg")
        double weight,

        @Min(value = 20, message = "Height must be at least 20 cm")
        @Max(value = 200, message = "Height cannot exceed 200 cm")
        short height,

        @NotNull(message = "Gender is required")
        GenderEnum gender,

        @NotNull(message = "Goal is required")
        GoalEnum goal
) {}
