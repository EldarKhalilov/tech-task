package com.theus.tt.service;

import com.theus.tt.dto.DailyNutritionProjection;
import com.theus.tt.dto.response.DailyReportRecord;
import com.theus.tt.dto.response.NutritionHistoryRecord;
import com.theus.tt.repository.MealRepository;
import com.theus.tt.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private final Clock fixedClock = Clock.fixed(
            Instant.parse("2024-01-01T00:00:00Z"),
            ZoneId.systemDefault()
    );

    @Test
    void generateDailyReport_ShouldReturnCorrectData() throws Exception {
        LocalDate date = LocalDate.of(2024, 1, 1);
        when(mealRepository.getDailyCaloriesSum(1L, date.atStartOfDay(), date.atTime(23, 59, 59)))
                .thenReturn(1800.0);
        when(customerService.calculateDailyCalories(1L)).thenReturn(2000.0);

        DailyReportRecord result = reportService.generateDailyReport(1L, date);

        assertAll(
                () -> assertEquals(date, result.date()),
                () -> assertEquals(1800.0, result.totalCalories()),
                () -> assertEquals(2000.0, result.dailyNorm()),
                () -> assertTrue(result.isWithinLimit())
        );
    }

    @Test
    void generateDailyReport_ShouldHandleNullCalories() throws Exception {
        LocalDate date = LocalDate.now(fixedClock);
        when(mealRepository.getDailyCaloriesSum(anyLong(), any(), any())).thenReturn(null);
        when(customerService.calculateDailyCalories(anyLong())).thenReturn(2500.0);

        DailyReportRecord result = reportService.generateDailyReport(1L, date);

        assertAll(
                () -> assertEquals(0.0, result.totalCalories()),
                () -> assertFalse(result.isWithinLimit())
        );
    }

    @Test
    void getNutritionHistory_ShouldFillMissingDays() {
        reportService.setClock(fixedClock);

        List<DailyNutritionProjection> projections = Arrays.asList(
                new DailyNutritionProjection(LocalDate.of(2023, 12, 31), 1500.0, 2L),
                new DailyNutritionProjection(LocalDate.of(2024, 1, 1), 2000.0, 3L)
        );

        when(mealRepository.getNutritionHistoryData(anyLong(), any(), any()))
                .thenReturn(projections);

        List<NutritionHistoryRecord.DailySummary> result =
                reportService.getNutritionHistory(1L, 3);

        assertAll(
                () -> assertEquals(3, result.size()),
                () -> assertEquals(LocalDate.of(2023, 12, 30), result.get(0).date()),
                () -> assertEquals(0.0, result.get(0).totalCalories()),
                () -> assertEquals(1500.0, result.get(1).totalCalories()),
                () -> assertEquals(3, result.get(2).mealsCount())
        );
    }

    @Test
    void getNutritionHistory_ShouldHandleEmptyData() {
        reportService.setClock(fixedClock);
        when(mealRepository.getNutritionHistoryData(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        List<NutritionHistoryRecord.DailySummary> result =
                reportService.getNutritionHistory(1L, 2);

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(r -> r.totalCalories() == 0.0))
        );
    }

    @Test
    void getNutritionHistory_ShouldHandleExactDays() {
        reportService.setClock(fixedClock);
        when(mealRepository.getNutritionHistoryData(anyLong(), any(), any()))
                .thenReturn(List.of(
                        new DailyNutritionProjection(LocalDate.of(2024, 1, 1), 2500.0, 4L)
                ));

        List<NutritionHistoryRecord.DailySummary> result =
                reportService.getNutritionHistory(1L, 1);

        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals(2500.0, result.get(0).totalCalories())
        );
    }
}
