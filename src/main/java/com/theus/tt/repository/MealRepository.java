package com.theus.tt.repository;

import com.theus.tt.entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<MealEntity, Long> {
    List<MealEntity> findByCustomerIdAndMealTimeBetween
            (Long customerId, LocalDateTime mealTime, LocalDateTime mealTime2);
}
