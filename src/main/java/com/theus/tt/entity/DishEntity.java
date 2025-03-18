package com.theus.tt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "dishes")
@EqualsAndHashCode(callSuper = false)
public class DishEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "dish_seq", sequenceName = "dish_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Min(0)
    @Column(nullable = false)
    private short caloriesPerPortion;

    @Min(0)
    private double proteins;

    @Min(0)
    private double fats;

    @Min(0)
    private double carbohydrates;

    @OneToMany(mappedBy = "dish")
    private List<MealDishEntity> mealDishes = new ArrayList<>();
}
