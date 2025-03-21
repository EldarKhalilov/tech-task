package com.theus.tt.controller;

import com.theus.tt.dto.request.DishCreateRecord;
import com.theus.tt.service.DishService;
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
@RequestMapping("/api/v1/dishes")
public class DishController {
    private final DishService dishService;

    @Operation(
            summary = "Создать блюдо",
            description = "Создает новое блюдо в системе и сохраняет в Бд"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Блюдо создано"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping("/create")
    public ResponseEntity<String> createDish(@Valid @RequestBody DishCreateRecord dto) {
        log.info("Incoming create dish request, dish name: {}", dto.name());
        dishService.createDish(dto);
        log.debug("Outgoing create dish response");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Dish created successfully");
    }
}
