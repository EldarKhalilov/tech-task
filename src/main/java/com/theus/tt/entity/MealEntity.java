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

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "meals")
@EqualsAndHashCode(callSuper = false)
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

    // Расчет общей питательной ценности
    public double getTotalCalories() {
        return dishes.stream().mapToDouble(MealDishEntity::getTotalCalories).sum();
    }

    // Добавление блюда в прием пищи
    public void addDish(DishEntity dish, int portions) {
        MealDishEntity mealDish = new MealDishEntity();
        mealDish.setMeal(this);
        mealDish.setDish(dish);
        mealDish.setPortions(portions);
        dishes.add(mealDish);
    }
}
