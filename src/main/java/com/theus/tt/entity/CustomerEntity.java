package com.theus.tt.entity;

import com.theus.tt.entity.enums.GenderEnum;
import com.theus.tt.entity.enums.GoalEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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

    @NotBlank
    private String name;

    @Email @Column(unique = true)
    private String email;

    @Min(9) @Max(120)
    private byte age;

    @Min(20) @Max(300)
    private short height;

    @Min(30) @Max(500)
    private double weight;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Enumerated(EnumType.STRING)
    private GoalEnum goal;

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
