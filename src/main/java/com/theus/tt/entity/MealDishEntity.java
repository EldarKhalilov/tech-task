package com.theus.tt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "meal_dishes")
@EqualsAndHashCode(callSuper = false)
public class MealDishEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "meal_dish_seq", sequenceName = "meal_dish_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private MealEntity meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private DishEntity dish;

    @Min(1)
    private int portions;

    // Расчет калорий для указанного количества порций
    public double getTotalCalories() {
        return dish.getCaloriesPerPortion() * portions;
    }
}
