package com.theus.tt.service;

import com.theus.tt.dto.request.DishCreateRecord;
import com.theus.tt.entity.DishEntity;
import com.theus.tt.exception.DishNotFoundException;

public interface DishService {
    void createDish(DishCreateRecord dto);
    boolean existsById(Long id);
    DishEntity getDishById(Long id) throws DishNotFoundException;
}
