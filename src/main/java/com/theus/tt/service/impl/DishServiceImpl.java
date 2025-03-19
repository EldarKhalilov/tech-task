package com.theus.tt.service.impl;

import com.theus.tt.dto.request.DishCreateRecord;
import com.theus.tt.entity.DishEntity;
import com.theus.tt.exception.DishNotFoundException;
import com.theus.tt.repository.DishRepository;
import com.theus.tt.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;

    @Override
    public void createDish(DishCreateRecord dto) {

        var dish = new DishEntity();
        dish.setName(dto.name());
        dish.setCaloriesPerPortion(dto.caloriesPerPortion());
        dish.setProteins(dto.proteins());
        dish.setFats(dto.fats());
        dish.setCarbohydrates(dto.carbohydrates());

        dishRepository.save(dish);
    }

    @Override
    public DishEntity getDishById(Long id) throws DishNotFoundException {
        return dishRepository.findById(id)
                .orElseThrow(DishNotFoundException::new);
    }

    @Override
    public boolean existsById(Long id) {
        return dishRepository.existsById(id);
    }
}
