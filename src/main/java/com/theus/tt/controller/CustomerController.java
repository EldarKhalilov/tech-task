package com.theus.tt.controller;

import com.theus.tt.dto.request.UserCreateRecord;
import com.theus.tt.service.CustomerService;
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
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Operation(
            summary = "Создать пользователя",
            description = "Регистрирует нового пользователя и сохраняет в Бд"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя")
    })
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserCreateRecord dto) throws Exception {
        log.info("Incoming create customer request, customer name: {}", dto.name());
        customerService.createUser(dto);
        log.debug("Outgoing create customer response");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully");
    }
}
