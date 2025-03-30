package com.theus.tt.service.impl;

import com.theus.tt.dto.request.DishCreateRecord;
import com.theus.tt.entity.DishEntity;
import com.theus.tt.mapper.DishMapper;
import com.theus.tt.repository.DishRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DishServiceImplTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private DishMapper dishMapper;

    @InjectMocks
    private DishServiceImpl dishService;

    @Test
    void createDish_ShouldSaveEntity() {
        DishCreateRecord dto = new DishCreateRecord(
                "Salad", (short) 100, 5.0, 3.0, 10.0);
        DishEntity entity = new DishEntity();
        when(dishMapper.toEntity(dto)).thenReturn(entity);

        dishService.createDish(dto);

        verify(dishRepository).save(entity);
    }

    @Test
    void existsById_ShouldReturnTrueWhenExists() {
        when(dishRepository.existsById(1L)).thenReturn(true);

        assertTrue(dishService.existsById(1L));
    }

    @Test
    void findAllById_ShouldReturnCorrectDishes() {
        List<Long> ids = List.of(1L, 2L);
        DishEntity dish1 = new DishEntity();
        DishEntity dish2 = new DishEntity();
        when(dishRepository.findAllById(ids)).thenReturn(List.of(dish1, dish2));

        List<DishEntity> result = dishService.findAllById(ids);

        assertEquals(2, result.size());
    }
}
