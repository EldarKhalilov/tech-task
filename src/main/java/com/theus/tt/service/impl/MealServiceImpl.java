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

    @Override
    public List<MealEntity> getMealsByUserAndDate(Long userId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
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

        for (MealDishRequest dishReq : dishes) {
            if (!dishService.existsById(dishReq.dishId())) {
                throw new DishNotFoundException("Dish not found: " + dishReq.dishId());
            }
        }
    }
}
