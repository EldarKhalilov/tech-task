package com.theus.tt.mapper;

import com.theus.tt.dto.request.DishCreateRecord;
import com.theus.tt.entity.DishEntity;
import org.springframework.stereotype.Component;

@Component
public class DishMapper {

    public DishEntity toEntity(DishCreateRecord dto) {
        if ( dto == null ) {
            return null;
        }

        DishEntity dishEntity = new DishEntity();

        dishEntity.setName( dto.name() );
        dishEntity.setCaloriesPerPortion( dto.caloriesPerPortion() );
        dishEntity.setProteins( dto.proteins() );
        dishEntity.setFats( dto.fats() );
        dishEntity.setCarbohydrates( dto.carbohydrates() );

        return dishEntity;
    }
}
