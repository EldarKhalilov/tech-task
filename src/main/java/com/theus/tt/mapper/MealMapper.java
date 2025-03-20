package com.theus.tt.mapper;

import com.theus.tt.dto.request.MealEntryRecord;
import com.theus.tt.dto.response.DailyReportRecord;
import com.theus.tt.entity.DishEntity;
import com.theus.tt.entity.MealEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MealMapper {
    DailyReportRecord.MealInfo mealEntityToMealInfo(MealEntity meal);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "dishes", ignore = true)
    MealEntity toEntity(MealEntryRecord dto);

    default DailyReportRecord.DishInfo mapDish(DishEntity dish, Integer portions, Double calories) {
        return new DailyReportRecord.DishInfo(dish.getName(), portions, calories);
    }
}
