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
@Table(name = "customers", indexes = {
        @Index(name = "idx_customers_email", columnList = "email", unique = true)
})
@EqualsAndHashCode(callSuper = true)
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
}
