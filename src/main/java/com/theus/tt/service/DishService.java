package com.theus.tt.service;

import com.theus.tt.dto.request.DishCreateRecord;
import com.theus.tt.entity.DishEntity;

import java.util.List;

public interface DishService {
    void createDish(DishCreateRecord dto);
    boolean existsById(Long id);
    List<DishEntity> findAllById(List<Long> ids);
}
