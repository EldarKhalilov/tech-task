package com.theus.tt.service.impl;

import com.theus.tt.dto.request.MealDishRequest;
import com.theus.tt.dto.request.MealEntryRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.entity.DishEntity;
import com.theus.tt.entity.MealEntity;
import com.theus.tt.exception.CustomerNotFoundException;
import com.theus.tt.exception.DishNotFoundException;
import com.theus.tt.mapper.MealMapper;
import com.theus.tt.repository.MealRepository;
import com.theus.tt.service.CustomerService;
import com.theus.tt.service.DishService;
import com.theus.tt.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {
    private final MealRepository repository;
    private final DishService dishService;
    private final CustomerService customerService;
    private final MealMapper mapper;

    @Override
    public void createMeal(MealEntryRecord dto)
            throws CustomerNotFoundException, DishNotFoundException {

        CustomerEntity user = customerService.getById(dto.userId());

        validateDishesExist(dto.dishes());

        MealEntity meal = mapper.toEntity(dto);
        meal.setCustomer(user);
        addDishesToMeal(meal, dto.dishes());

        repository.save(meal);
    }

    // для одного запроса на конкретный день
    @Override
    public List<MealEntity> getMealsByUserAndDate(Long userId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        return repository.findByCustomerIdAndMealTimeBetween(userId, start, end);
    }

    // для нескольких запросов по дням
    @Override
    public List<MealEntity> getMealsByUserAndDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        return repository.findByCustomerIdAndMealTimeBetween(userId, start, end);
    }

    private void addDishesToMeal(MealEntity meal, List<MealDishRequest> dishes)
            throws DishNotFoundException {

        for (MealDishRequest dishReq : dishes) {
            DishEntity dish = dishService.getDishById(dishReq.dishId());
            meal.addDish(dish, dishReq.portions());
        }
    }

    private void validateDishesExist(List<MealDishRequest> dishes)
            throws DishNotFoundException {
        boolean allDishesExist = dishes.stream()
                .allMatch(dishReq -> dishService.existsById(dishReq.dishId()));

        if (!allDishesExist) {
            List<Long> missingIds = dishes.stream()
                    .map(MealDishRequest::dishId)
                    .filter(id -> !dishService.existsById(id))
                    .toList();
            throw new DishNotFoundException("Dishes not found: " + missingIds);
        }
    }
}
