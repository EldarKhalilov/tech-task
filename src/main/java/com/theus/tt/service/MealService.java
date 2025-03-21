package com.theus.tt.service;

import com.theus.tt.dto.request.MealEntryRecord;
import com.theus.tt.entity.MealEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface MealService {
    void createMeal(MealEntryRecord dto);
    List<MealEntity> getMealsByUserAndDateRange(Long userId, LocalDateTime start, LocalDateTime end);
}
