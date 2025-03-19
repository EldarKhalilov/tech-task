package com.theus.tt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meal_dishes")
@EqualsAndHashCode(callSuper = false)
public class MealDishEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "meal_dish_seq", sequenceName = "meal_dish_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private MealEntity meal;

    @ManyToOne(fetch = FetchType.LAZY)
    private DishEntity dish;

    @Min(1) private int portions;

    public double getTotalCalories() {
        if (dish == null) {
            throw new IllegalStateException("Dish is not set for MealDishEntity id=" + this.id);
        }
        return dish.getCaloriesPerPortion() * portions;
    }
}
