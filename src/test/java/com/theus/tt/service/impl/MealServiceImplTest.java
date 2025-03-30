package com.theus.tt.service.impl;

import com.theus.tt.dto.request.MealDishRequest;
import com.theus.tt.dto.request.MealEntryRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.entity.DishEntity;
import com.theus.tt.entity.MealEntity;
import com.theus.tt.entity.enums.MealType;
import com.theus.tt.mapper.MealMapper;
import com.theus.tt.repository.MealRepository;
import com.theus.tt.service.CustomerService;
import com.theus.tt.service.DishService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MealServiceImplTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private DishService dishService;

    @Mock
    private CustomerService customerService;

    @Mock
    private MealMapper mealMapper;

    @InjectMocks
    private MealServiceImpl mealService;

    @Test
    void createMeal_ShouldAddDishesAndSave() throws Exception {
        // Arrange
        Long dishId = 1L;
        MealEntryRecord dto = new MealEntryRecord(
                1L,
                MealType.LUNCH,
                LocalDateTime.now(),
                List.of(new MealDishRequest(dishId, 2))
        );

        CustomerEntity customer = new CustomerEntity();
        MealEntity meal = new MealEntity();

        DishEntity dish = new DishEntity();
        dish.setId(dishId);

        when(customerService.getById(1L)).thenReturn(customer);
        when(mealMapper.toEntity(dto)).thenReturn(meal);
        when(dishService.findAllById(anyList())).thenReturn(List.of(dish));

        mealService.createMeal(dto);

        verify(mealRepository).save(meal);
        assertEquals(1, meal.getDishes().size());
    }

    @Test
    void getMealsByUserAndDateRange_ShouldReturnFromRepository() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<MealEntity> expected = List.of(new MealEntity());
        when(mealRepository.findByCustomerIdAndMealTimeBetween(1L, start, end))
                .thenReturn(expected);

        List<MealEntity> result = mealService.getMealsByUserAndDateRange(1L, start, end);

        assertEquals(1, result.size());
    }
}
