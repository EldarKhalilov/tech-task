package com.theus.tt.controller;

import com.theus.tt.dto.response.DailyReportRecord;
import com.theus.tt.dto.response.NutritionHistoryRecord;
import com.theus.tt.exception.CustomerNotFoundException;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;

    @Operation(summary = "Дневной отчет", description = "Статистика по калориям за день")
    @GetMapping("/daily")
    public ResponseEntity<DailyReportRecord> getDailyReport(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) throws CustomerNotFoundException {
        return ResponseEntity.ok(reportService.generateDailyReport(userId, date));
    }

    @Operation(summary = "Проверка лимита калорий")
    @GetMapping("/daily-check")
    public ResponseEntity<Boolean> checkDailyLimit(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) throws CustomerNotFoundException {
        DailyReportRecord report = reportService.generateDailyReport(userId, date);
        return ResponseEntity.ok(report.isWithinLimit());
    }

    @Operation(summary = "История питания")
    @GetMapping("/history")
    public ResponseEntity<NutritionHistoryRecord> getHistory(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "7") int days
    ) throws CustomerNotFoundException {
        return ResponseEntity.ok(reportService.getNutritionHistory(userId, days));
    }
}
