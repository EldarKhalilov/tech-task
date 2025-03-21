package com.theus.tt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "dishes")
@EqualsAndHashCode(callSuper = true)
public class DishEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "dish_seq", sequenceName = "dish_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    private String name;

    @Min(1)
    private short caloriesPerPortion;

    @Min(1)
    private double proteins;

    @Min(1)
    private double fats;

    @Min(1)
    private double carbohydrates;
}
