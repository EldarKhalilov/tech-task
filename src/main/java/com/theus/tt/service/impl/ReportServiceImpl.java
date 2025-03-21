package com.theus.tt.service.impl;

import com.theus.tt.dto.DailyNutritionProjection;
import com.theus.tt.dto.response.DailyReportRecord;
import com.theus.tt.dto.response.NutritionHistoryRecord;
import com.theus.tt.repository.MealRepository;
import com.theus.tt.service.CustomerService;
import com.theus.tt.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final CustomerService customerService;
    private final MealRepository mealRepository;

    @Setter
    private Clock clock = Clock.systemDefaultZone();

    @Override
    @Transactional(readOnly = true)
    public DailyReportRecord generateDailyReport(Long userId, LocalDate date) {
        log.info("Generating daily report for user ID: {} date: {}", userId, date);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        Double totalCalories = mealRepository.getDailyCaloriesSum(userId, start, end);
        log.debug("Total calories: {} for user ID: {}", totalCalories, userId);

        double dailyNorm = customerService.calculateDailyCalories(userId);
        log.debug("Daily norm: {} for user ID: {}", dailyNorm, userId);

        return new DailyReportRecord(
                date,
                totalCalories != null ? totalCalories : 0.0,
                dailyNorm,
                totalCalories != null && totalCalories <= dailyNorm,
                Collections.emptyList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<NutritionHistoryRecord.DailySummary> getNutritionHistory(Long userId, int days) {
        log.info("Getting nutrition history for user ID: {} days: {}", userId, days);
        LocalDate endDate = LocalDate.now(clock);
        LocalDate startDate = endDate.minusDays(days - 1);

        List<DailyNutritionProjection> results = mealRepository.getNutritionHistoryData(
                userId,
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );

        log.debug("Received {} daily nutrition projections", results.size());

        Map<LocalDate, DailyStats> statsMap = results.stream()
                .collect(Collectors.toMap(
                        DailyNutritionProjection::date,
                        d -> new DailyStats(d.calories(), d.mealCount())
                ));

        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(days)
                .map(date -> {
                    DailyStats stats = statsMap.getOrDefault(date, new DailyStats(0.0, 0L));
                    return new NutritionHistoryRecord.DailySummary(
                            date,
                            stats.calories(),
                            stats.mealCount()
                    );
                })
                .toList();
    }

    private record DailyStats(Double calories, Long mealCount) {}
}
