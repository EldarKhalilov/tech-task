package com.theus.tt.service.impl;

import com.theus.tt.dto.request.MealDishRequest;
import com.theus.tt.dto.request.MealEntryRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.entity.DishEntity;
import com.theus.tt.entity.MealEntity;
import com.theus.tt.exception.notfound.DishNotFoundException;
import com.theus.tt.exception.notfound.MealNotFoundException;
import com.theus.tt.mapper.MealMapper;
import com.theus.tt.repository.MealRepository;
import com.theus.tt.service.BaseService;
import com.theus.tt.service.CustomerService;
import com.theus.tt.service.DishService;
import com.theus.tt.service.MealService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Service
public class MealServiceImpl extends BaseService<MealEntity, Long> implements MealService {
    private final MealRepository mealRepository;
    private final DishService dishService;
    private final CustomerService customerService;
    private final MealMapper mapper;

    public MealServiceImpl(
            MealRepository mealRepository,
            DishService dishService,
            CustomerService customerService,
            MealMapper mapper) {
        super(
                mealRepository,
                MealNotFoundException::new
        );
        this.mealRepository = mealRepository;
        this.dishService = dishService;
        this.customerService = customerService;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void createMeal(MealEntryRecord dto) throws DishNotFoundException {
        CustomerEntity user = customerService.getById(dto.userId());

        MealEntity meal = mapper.toEntity(dto);
        meal.setCustomer(user);
        addDishesToMeal(meal, dto.dishes());

        repository.save(meal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealEntity> getMealsByUserAndDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        return mealRepository.findByCustomerIdAndMealTimeBetween(userId, start, end);
    }

    private void addDishesToMeal(MealEntity meal, List<MealDishRequest> dishes)
            throws DishNotFoundException {
        List<Long> dishIds = dishes.stream()
                .map(MealDishRequest::dishId)
                .toList();

        Map<Long, DishEntity> dishMap = dishService.findAllById(dishIds).stream()
                .collect(toMap(DishEntity::getId, Function.identity()));

        for (MealDishRequest dishReq : dishes) {
            DishEntity dish = Optional.ofNullable(dishMap.get(dishReq.dishId()))
                    .orElseThrow(() -> new DishNotFoundException(dishReq.dishId()));

            meal.addDish(dish, dishReq.portions());
        }
    }
}
