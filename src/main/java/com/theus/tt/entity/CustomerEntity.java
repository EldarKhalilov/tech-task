package com.theus.tt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
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
@Table(name = "customers")
@EqualsAndHashCode(callSuper = false)
public class CustomerEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "customer_seq", sequenceName = "customer_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Min(9) @Max(120)
    private byte age;

    @Min(70) @Max(220)
    private double weight;

    @Min(20) @Max(200)
    private short height;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GoalEnum goal;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealEntity> meals = new ArrayList<>();

    public double calculateDailyCalories() {
        double bmr = calculateBMR();
        double activityFactor = 1.2; // Базовый коэффициент активности

        return switch (goal) {
            case MAINTENANCE -> bmr * activityFactor;
            case WEIGHT_LOSS -> bmr * activityFactor - 500;
            case MASS_GAIN ->  bmr * activityFactor + 500;
        };
    }

    private double calculateBMR() {
        return gender == GenderEnum.MALE ?
                88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age) :
                447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
    }
}
