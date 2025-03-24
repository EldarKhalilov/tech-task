package com.theus.tt.service;

import com.theus.tt.dto.request.MealEntryRecord;
import com.theus.tt.entity.MealEntity;
import com.theus.tt.exception.notfound.CustomerNotFoundException;
import com.theus.tt.exception.notfound.DishNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface MealService {
    void createMeal(MealEntryRecord dto) throws CustomerNotFoundException, DishNotFoundException;
    List<MealEntity> getMealsByUserAndDateRange(Long userId, LocalDateTime start, LocalDateTime end);
}
