package com.theus.tt.service;

import com.theus.tt.dto.response.DailyReportRecord;
import com.theus.tt.dto.response.NutritionHistoryRecord;
import com.theus.tt.exception.CustomerNotFoundException;

import java.time.LocalDate;

public interface ReportService {
    DailyReportRecord generateDailyReport(Long userId, LocalDate date) throws CustomerNotFoundException;
    NutritionHistoryRecord getNutritionHistory(Long userId, int days) throws CustomerNotFoundException;
}
