package com.theus.tt.controller;

import com.theus.tt.dto.DailyReportRecord;
import com.theus.tt.dto.MealEntryRecord;
import com.theus.tt.dto.NutritionHistoryRecord;
import com.theus.tt.dto.UserCreateRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.entity.MealEntity;
import com.theus.tt.exception.CustomerNotFoundException;
import com.theus.tt.service.NutritionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Получить дневной отчет",
            description = "Возвращает полную статистику по калориям и приемам пищи за указанный день"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Отчет успешно сформирован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/reports/daily")
    public ResponseEntity<DailyReportRecord> getDailyReport(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) throws CustomerNotFoundException {
        return ResponseEntity.ok(nutritionService.generateDailyReport(userId, date));
    }

    @Operation(
            summary = "Проверить лимит калорий",
            description = "Возвращает true/false в зависимости от соблюдения дневной нормы"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проверка выполнена"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/reports/daily-check")
    public ResponseEntity<Boolean> checkDailyLimit(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) throws CustomerNotFoundException {
        DailyReportRecord report = nutritionService.generateDailyReport(userId, date);
        return ResponseEntity.ok(report.isWithinLimit());
    }

    @Operation(
            summary = "Получить историю питания",
            description = "Возвращает агрегированные данные по калориям за указанное количество дней"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные получены"),
            @ApiResponse(responseCode = "400", description = "Некорректный параметр дней")
    })
    @GetMapping("/get-history")
    public ResponseEntity<NutritionHistoryRecord> getNutritionHistory(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "7") int days
    ) {
        return ResponseEntity.ok(nutritionService.getNutritionHistory(userId, days));
    }

    @Operation(
            summary = "Добавить прием пищи",
            description = "Создает новую запись о приеме пищи с указанными блюдами"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Прием пищи создан"),
            @ApiResponse(responseCode = "404", description = "Пользователь или блюдо не найдены")
    })
    @PostMapping("/meals/add")
    public ResponseEntity<MealEntity> addMeal(@RequestBody MealEntryRecord dto)
            throws CustomerNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nutritionService.addMealEntry(dto));
    }

    @Operation(
            summary = "Создать пользователя",
            description = "Регистрирует нового пользователя в системе"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя")
    })
    @PostMapping("/users/create")
    public ResponseEntity<CustomerEntity> createUser(@RequestBody UserCreateRecord dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nutritionService.createUser(dto));
    }
}
