package com.theus.tt.entity;

import com.theus.tt.entity.enums.MealType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "meals", indexes = {
        @Index(name = "idx_meals_customer_meal_time", columnList = "customer_id, meal_time"),
        @Index(name = "idx_meals_meal_type", columnList = "meal_type")
})
@EqualsAndHashCode(callSuper = true)
public class MealEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "meal_seq", sequenceName = "meal_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerEntity customer;

    private LocalDateTime mealTime;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    private List<MealDishEntity> dishes = new ArrayList<>();

    // Добавление блюда в прием пищи
    public void addDish(DishEntity dish, int portions) {
        Objects.requireNonNull(dish, "Dish cannot be null");
        if (portions < 1) {
            throw new IllegalArgumentException("Portions must be ≥1");
        }
        dishes.add(new MealDishEntity(this, dish, portions));
    }
}
