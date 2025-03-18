package com.theus.tt.dto;

import com.theus.tt.entity.GenderEnum;
import com.theus.tt.entity.GoalEnum;

public record UserCreateRecord(
        String name,
        String email,
        Byte age,
        Short height,
        Double weight,
        GenderEnum gender,
        GoalEnum goal
) {}
