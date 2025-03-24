package com.theus.tt.controller;

import com.theus.tt.dto.request.MealEntryRecord;
import com.theus.tt.exception.notfound.CustomerNotFoundException;
import com.theus.tt.exception.notfound.DishNotFoundException;
import com.theus.tt.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meals")
public class MealController {
    private final MealService mealService;

    @Operation(
            summary = "Добавить прием пищи",
            description = "Создает запись о приеме пищи с указанными блюдами"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Прием пищи создан"),
            @ApiResponse(responseCode = "404", description = "Пользователь или блюдо не найдены"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping("/create")
    public ResponseEntity<String> createMealEntry(@Valid @RequestBody MealEntryRecord dto) throws Exception {
        log.info("Incoming create meal request, meal type: {}", dto.mealType());
        mealService.createMeal(dto);
        log.debug("Outgoing create meal response");
        return ResponseEntity.status(HttpStatus.CREATED).body("Meal added");
    }
}
