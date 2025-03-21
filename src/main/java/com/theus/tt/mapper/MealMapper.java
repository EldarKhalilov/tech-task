package com.theus.tt.mapper;

import com.theus.tt.dto.request.MealEntryRecord;
import com.theus.tt.entity.MealEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MealMapper {
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "dishes", ignore = true)
    MealEntity toEntity(MealEntryRecord dto);
}
