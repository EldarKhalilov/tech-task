package com.theus.tt.service.impl;

import com.theus.tt.dto.response.DailyReportRecord;
import com.theus.tt.dto.response.NutritionHistoryRecord;
import com.theus.tt.entity.MealEntity;
import com.theus.tt.exception.CustomerNotFoundException;
import com.theus.tt.mapper.MealMapper;
import com.theus.tt.service.CustomerService;
import com.theus.tt.service.MealService;
import com.theus.tt.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final MealService mealService;
    private final CustomerService customerService;
    private final MealMapper mapper;

    @Override
    public DailyReportRecord generateDailyReport(Long userId, LocalDate date) throws CustomerNotFoundException {
        double dailyNorm = customerService.calculateDailyCalories(userId);
        List<MealEntity> meals = mealService.getMealsByUserAndDate(userId, date);

        double totalCalories = meals.stream()
                .mapToDouble(MealEntity::getTotalCalories)
                .sum();

        return new DailyReportRecord(date,
                totalCalories,
                dailyNorm,
                totalCalories <= dailyNorm,
                meals.stream().map(mapper::mealEntityToMealInfo).toList()
        );
    }

    @Override
    public NutritionHistoryRecord getNutritionHistory(Long userId, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        List<NutritionHistoryRecord.DailySummary> history = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            List<MealEntity> meals = mealService.getMealsByUserAndDate(userId, date);
            double totalCalories = meals.stream()
                    .mapToDouble(MealEntity::getTotalCalories)
                    .sum();
            history.add(new NutritionHistoryRecord.DailySummary(date, totalCalories, meals.size()));
        }

        return new NutritionHistoryRecord(history);
    }
}
