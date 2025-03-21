package com.theus.tt.controller;

import com.theus.tt.dto.response.DailyReportRecord;
import com.theus.tt.dto.response.NutritionHistoryRecord;
import com.theus.tt.exception.notfound.CustomerNotFoundException;
import com.theus.tt.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;

    @Operation(summary = "Дневной отчет", description = "Статистика по калориям за день")
    @GetMapping("/daily")
    public ResponseEntity<DailyReportRecord> getDailyReport(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportService.generateDailyReport(userId, date));
    }

    @Operation(summary = "Проверка лимита калорий")
    @GetMapping("/daily-check")
    public ResponseEntity<Boolean> checkDailyLimit(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyReportRecord report = reportService.generateDailyReport(userId, date);
        return ResponseEntity.ok(report.isWithinLimit());
    }

    @Operation(summary = "История питания")
    @GetMapping("/history")
    public ResponseEntity<List<NutritionHistoryRecord.DailySummary>> getHistory(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(reportService.getNutritionHistory(userId, days));
    }
}
