package com.theus.tt.service;

import com.theus.tt.dto.response.DailyReportRecord;
import com.theus.tt.dto.response.NutritionHistoryRecord;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    DailyReportRecord generateDailyReport(Long userId, LocalDate date);
    List<NutritionHistoryRecord.DailySummary> getNutritionHistory(Long userId, int days);
}
