package com.theus.tt.util;

import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.entity.enums.GenderEnum;
import org.springframework.stereotype.Component;

@Component
public class CaloriesCalculatorUtil {
    public double calculateDailyCalories(CustomerEntity user) {
        double bmr = calculateBMR(user);
        return switch (user.getGoal()) {
            case MAINTENANCE -> bmr * 1.2;
            case WEIGHT_LOSS -> Math.max(bmr * 1.2 - 500, bmr * 0.8);
            case MASS_GAIN -> bmr * 1.2 + 500;
        };
    }

    private double calculateBMR(CustomerEntity user) {
        return user.getGender() == GenderEnum.MALE ?
                88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * user.getAge()) :
                447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * user.getAge());
    }
}
