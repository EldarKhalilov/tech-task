package com.theus.tt.mapper;

import com.theus.tt.dto.request.MealEntryRecord;
import com.theus.tt.entity.MealEntity;
import org.springframework.stereotype.Component;

@Component
public class MealMapper {

    public MealEntity toEntity(MealEntryRecord dto) {
        if ( dto == null ) {
            return null;
        }

        MealEntity mealEntity = new MealEntity();

        mealEntity.setMealTime( dto.mealTime() );
        mealEntity.setMealType( dto.mealType() );

        return mealEntity;
    }
}
