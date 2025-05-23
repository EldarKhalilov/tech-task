package com.theus.tt.service;

import com.theus.tt.dto.response.DailyReportRecord;
import com.theus.tt.dto.response.NutritionHistoryRecord;
import com.theus.tt.exception.notfound.CustomerNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    DailyReportRecord generateDailyReport(Long userId, LocalDate date) throws CustomerNotFoundException;
    List<NutritionHistoryRecord.DailySummary> getNutritionHistory(Long userId, int days);
}
