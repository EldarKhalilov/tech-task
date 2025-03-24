package com.theus.tt.repository;

import com.theus.tt.dto.DailyNutritionProjection;
import com.theus.tt.entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<MealEntity, Long> {
    List<MealEntity> findByCustomerIdAndMealTimeBetween(
            Long customerId, LocalDateTime mealTimeStart, LocalDateTime mealTimeEnd);

    @Query("""
        SELECT \s
            SUM(md.dish.caloriesPerPortion * md.portions) as totalCalories
        FROM MealEntity m
        JOIN m.dishes md
        WHERE m.customer.id = :userId\s
          AND m.mealTime BETWEEN :start AND :end
    """)
    Double getDailyCaloriesSum(@Param("userId") Long userId,
                               @Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);

    @Query("""
    SELECT NEW com.theus.tt.dto.DailyNutritionProjection(
        CAST(m.mealTime AS localdate),
        SUM(md.dish.caloriesPerPortion * md.portions),
        COUNT(DISTINCT m.id)
    )
    FROM MealEntity m
    JOIN m.dishes md
    WHERE m.customer.id = :userId 
      AND m.mealTime BETWEEN :start AND :end
    GROUP BY CAST(m.mealTime AS localdate)
    """)
    List<DailyNutritionProjection> getNutritionHistoryData(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
