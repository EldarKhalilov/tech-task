package com.theus.tt.service.impl;

import com.theus.tt.dto.request.MealDishRequest;
import com.theus.tt.dto.request.MealEntryRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.entity.DishEntity;
import com.theus.tt.entity.MealEntity;
import com.theus.tt.exception.notfound.CustomerNotFoundException;
import com.theus.tt.exception.notfound.DishNotFoundException;
import com.theus.tt.mapper.MealMapper;
import com.theus.tt.repository.MealRepository;
import com.theus.tt.service.CustomerService;
import com.theus.tt.service.DishService;
import com.theus.tt.service.MealService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {
    private final MealRepository mealRepository;
    private final DishService dishService;
    private final CustomerService customerService;
    private final MealMapper mapper;

    @Override
    @Transactional
    public void createMeal(MealEntryRecord dto) throws CustomerNotFoundException, DishNotFoundException {
        log.info("Creating meal");
        try {
            CustomerEntity user = customerService.getById(dto.userId());
            log.debug("Found user: {}", user.getId());

            MealEntity meal = mapper.toEntity(dto);
            meal.setCustomer(user);
            addDishesToMeal(meal, dto.dishes());

            mealRepository.save(meal);
            log.info("Created meal ID: {}", meal.getId());
        } catch (Exception e) {
            log.error("Error creating meal: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealEntity> getMealsByUserAndDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        log.debug("Getting meals for user ID: {} between {} and {}", userId, start, end);
        List<MealEntity> meals = mealRepository.findByCustomerIdAndMealTimeBetween(userId, start, end);
        log.info("Found {} meals for user ID: {}", meals.size(), userId);
        return meals;
    }

    private void addDishesToMeal(MealEntity meal, List<MealDishRequest> dishes) throws DishNotFoundException {
        log.debug("Adding {} dishes to meal", dishes.size());
        List<Long> dishIds = dishes.stream()
                .map(MealDishRequest::dishId)
                .toList();

        Map<Long, DishEntity> dishMap = dishService.findAllById(dishIds).stream()
                .collect(toMap(DishEntity::getId, Function.identity()));

        for (MealDishRequest dishReq : dishes) {
            DishEntity dish = Optional.ofNullable(dishMap.get(dishReq.dishId()))
                    .orElseThrow(() -> {
                        log.error("Dish not found ID: {}", dishReq.dishId());
                        return new DishNotFoundException();
                    });

            meal.addDish(dish, dishReq.portions());
            log.trace("Added dish ID: {} with {} portions", dish.getId(), dishReq.portions());
        }
    }
}
