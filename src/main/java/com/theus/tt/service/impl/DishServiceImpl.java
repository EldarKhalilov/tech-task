package com.theus.tt.service.impl;

import com.theus.tt.dto.request.DishCreateRecord;
import com.theus.tt.entity.DishEntity;
import com.theus.tt.exception.DishNotFoundException;
import com.theus.tt.mapper.DishMapper;
import com.theus.tt.repository.DishRepository;
import com.theus.tt.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    private final DishRepository repository;
    private final DishMapper mapper;

    @Override
    public void createDish(DishCreateRecord dto) {
        DishEntity dish = mapper.toEntity(dto);
        repository.save(dish);
    }

    @Override
    public DishEntity getDishById(Long id) throws DishNotFoundException {
        return repository.findById(id)
                .orElseThrow(DishNotFoundException::new);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
