package com.theus.tt.controller;

import com.theus.tt.dto.DailyReportRecord;
import com.theus.tt.dto.MealEntryRecord;
import com.theus.tt.dto.NutritionHistoryRecord;
import com.theus.tt.dto.UserCreateRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.entity.MealEntity;
import com.theus.tt.exception.CustomerNotFoundException;
import com.theus.tt.service.NutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/nutrition")
public class NutritionController {
    private final NutritionService nutritionService;

    @GetMapping("/reports/daily")
    public ResponseEntity<DailyReportRecord> getDailyReport(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) throws CustomerNotFoundException {
        return ResponseEntity.ok(nutritionService.generateDailyReport(userId, date));
    }

    @GetMapping("/reports/daily-check")
    public ResponseEntity<Boolean> checkDailyLimit(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) throws CustomerNotFoundException {
        DailyReportRecord report = nutritionService.generateDailyReport(userId, date);
        return ResponseEntity.ok(report.isWithinLimit());
    }

    @GetMapping("/get-history")
    public ResponseEntity<NutritionHistoryRecord> getNutritionHistory(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "7") int days
    ) {
        return ResponseEntity.ok(nutritionService.getNutritionHistory(userId, days));
    }

    @PostMapping("/meals/add")
    public ResponseEntity<MealEntity> addMeal(@RequestBody MealEntryRecord dto)
            throws CustomerNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nutritionService.addMealEntry(dto));
    }

    @PostMapping("/users/create")
    public ResponseEntity<CustomerEntity> createUser(@RequestBody UserCreateRecord dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nutritionService.createUser(dto));
    }
}
