package com.theus.tt.service;

import com.theus.tt.dto.DailyReportRecord;
import com.theus.tt.dto.MealEntryRecord;
import com.theus.tt.dto.NutritionHistoryRecord;
import com.theus.tt.dto.UserCreateRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.entity.MealEntity;
import com.theus.tt.exception.CustomerNotFoundException;

import java.time.LocalDate;

public interface NutritionService {
    MealEntity addMealEntry(MealEntryRecord dto) throws CustomerNotFoundException;
    DailyReportRecord generateDailyReport(Long userId, LocalDate date) throws CustomerNotFoundException;
    NutritionHistoryRecord getNutritionHistory(Long userId, int days);
    CustomerEntity createUser(UserCreateRecord dto);
}
