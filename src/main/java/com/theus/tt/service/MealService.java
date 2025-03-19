package com.theus.tt.service;

import com.theus.tt.dto.request.MealEntryRecord;
import com.theus.tt.entity.MealEntity;
import com.theus.tt.exception.CustomerNotFoundException;
import com.theus.tt.exception.DishNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface MealService {
    void createMeal(MealEntryRecord dto) throws CustomerNotFoundException, DishNotFoundException;
    List<MealEntity> getMealsByUserAndDate(Long userId, LocalDate date);
}
