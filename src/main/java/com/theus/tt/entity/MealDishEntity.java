package com.theus.tt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "meal_dishes", indexes = {
        @Index(name = "idx_meal_dishes_meal", columnList = "meal_id"),
        @Index(name = "idx_meal_dishes_dish", columnList = "dish_id")
})
@EqualsAndHashCode(callSuper = true)
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

    public MealDishEntity(MealEntity meal,
                          DishEntity dish,
                          int portions) {
        this.meal = meal;
        this.dish = dish;
        this.portions = portions;
    }
}
