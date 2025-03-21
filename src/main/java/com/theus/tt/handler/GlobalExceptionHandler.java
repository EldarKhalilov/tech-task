package com.theus.tt.handler;

import com.theus.tt.exception.CustomerAlreadyExistsException;
import com.theus.tt.exception.notfound.CustomerNotFoundException;
import com.theus.tt.exception.notfound.DishNotFoundException;
import com.theus.tt.exception.notfound.MealNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Value("${spring.application.name}")
    private String appName;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", appName);

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("application name", appName);
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleCustomerAlreadyExists(Exception ex) {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("application name", appName);
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCustomerNotFound(Exception ex) {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("application name", appName);
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleDishNotFound(Exception ex) {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("application name", appName);
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MealNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMealNotFound(Exception ex) {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("application name", appName);
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
