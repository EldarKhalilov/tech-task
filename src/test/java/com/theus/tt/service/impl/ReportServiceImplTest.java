package com.theus.tt.service.impl;

import com.theus.tt.dto.DailyNutritionProjection;
import com.theus.tt.dto.response.DailyReportRecord;
import com.theus.tt.dto.response.NutritionHistoryRecord;
import com.theus.tt.repository.MealRepository;
import com.theus.tt.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private final Clock fixedClock = Clock.fixed(
            Instant.parse("2023-10-10T00:00:00Z"),
            ZoneId.systemDefault()
    );

    @BeforeEach
    void setUp() throws Exception {
        Field clockField = ReportServiceImpl.class.getDeclaredField("clock");
        clockField.setAccessible(true);
        clockField.set(reportService, fixedClock);
    }

    @Test
    void generateDailyReport_ShouldReturnCorrectData() throws Exception {
        LocalDate date = LocalDate.of(2023, 10, 10);
        when(mealRepository.getDailyCaloriesSum(1L, date.atStartOfDay(), date.atTime(23, 59, 59)))
                .thenReturn(1500.0);
        when(customerService.calculateDailyCalories(1L)).thenReturn(2000.0);

        DailyReportRecord result = reportService.generateDailyReport(1L, date);

        assertAll(
                () -> assertEquals(date, result.date()),
                () -> assertEquals(1500.0, result.totalCalories()),
                () -> assertEquals(2000.0, result.dailyNorm()),
                () -> assertTrue(result.isWithinLimit())
        );
    }

    @Test
    void generateDailyReport_ShouldHandleNullCalories() throws Exception {
        LocalDate date = LocalDate.now(fixedClock);
        when(mealRepository.getDailyCaloriesSum(any(), any(), any())).thenReturn(null);
        when(customerService.calculateDailyCalories(any())).thenReturn(2000.0);

        DailyReportRecord result = reportService.generateDailyReport(1L, date);

        assertEquals(0.0, result.totalCalories());
    }

    @Test
    void getNutritionHistory_ShouldFillMissingDays() {
        List<DailyNutritionProjection> projections = List.of(
                new DailyNutritionProjection(LocalDate.of(2023, 10, 9), 1500.0, 2L),
                new DailyNutritionProjection(LocalDate.of(2023, 10, 10), 2000.0, 3L)
        );

        when(mealRepository.getNutritionHistoryData(
                eq(1L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(projections);

        List<NutritionHistoryRecord.DailySummary> result =
                reportService.getNutritionHistory(1L, 3);

        assertAll(
                () -> assertEquals(3, result.size()),
                () -> assertEquals(LocalDate.of(2023, 10, 8), result.get(0).date()),
                () -> assertEquals(0.0, result.get(0).totalCalories()),
                () -> assertEquals(1500.0, result.get(1).totalCalories()),
                () -> assertEquals(3, result.get(2).mealsCount())
        );
    }

    @Test
    void getNutritionHistory_ShouldHandleEmptyData() {
        when(mealRepository.getNutritionHistoryData(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        List<NutritionHistoryRecord.DailySummary> result =
                reportService.getNutritionHistory(1L, 2);

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(r -> r.totalCalories() == 0.0))
        );
    }
}
