package com.theus.tt.service;

import com.theus.tt.dto.DailyReportRecord;
import com.theus.tt.dto.MealEntryRecord;
import com.theus.tt.dto.NutritionHistoryRecord;
import com.theus.tt.dto.UserCreateRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.entity.DishEntity;
import com.theus.tt.entity.MealEntity;
import com.theus.tt.exception.CustomerNotFoundException;
import com.theus.tt.exception.DishNotFoundException;
import com.theus.tt.repository.CustomerRepository;
import com.theus.tt.repository.DishRepository;
import com.theus.tt.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NutritionServiceImpl implements NutritionService {
    private final CustomerRepository customerRepository;
    private final DishRepository dishRepository;
    private final MealRepository mealRepository;

    /**
     * Добавляет новый прием пищи для указанного пользователя
     *
     * @param dto DTO с данными о приеме пищи
     * @return сохраненная сущность приема пищи
     * @throws CustomerNotFoundException если пользователь не найден
     * @throws DishNotFoundException если блюдо не найдено
     */
    @Override
    public MealEntity addMealEntry(MealEntryRecord dto) throws CustomerNotFoundException {
        CustomerEntity user = customerRepository.findById(dto.userId())
                .orElseThrow(CustomerNotFoundException::new);

        MealEntity meal = new MealEntity();
        meal.setCustomer(user);
        meal.setMealTime(dto.mealTime());
        meal.setMealType(dto.mealType());

        dto.dishes().forEach(dishRequest -> {
            try {
                DishEntity dish = dishRepository.findById(dishRequest.dishId())
                        .orElseThrow(DishNotFoundException::new);
                meal.addDish(dish, dishRequest.portions());
            } catch (DishNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        return mealRepository.save(meal);
    }

    /**
     * Генерирует дневной отчет по питанию
     *
     * @param userId ID пользователя
     * @param date дата для отчета
     * @return DTO с детализированными данными отчета
     * @throws CustomerNotFoundException если пользователь не найден
     */
    @Override
    public DailyReportRecord generateDailyReport(Long userId, LocalDate date)
            throws CustomerNotFoundException {
        CustomerEntity user = customerRepository.findById(userId)
                .orElseThrow(CustomerNotFoundException::new);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<MealEntity> meals = mealRepository.findByCustomerIdAndMealTimeBetween(userId, start, end);

        double totalCalories = meals.stream()
                .mapToDouble(MealEntity::getTotalCalories)
                .sum();

        double dailyNorm = user.calculateDailyCalories();

        return new DailyReportRecord(
                date,
                totalCalories,
                dailyNorm,
                totalCalories <= dailyNorm,
                meals.stream().map(this::mapMealToInfo).toList()
        );
    }

    /**
     * Возвращает историю питания за указанное количество дней
     *
     * @param userId ID пользователя
     * @param days количество дней для выборки
     * @return DTO с агрегированными данными по дням
     */
    @Override
    public NutritionHistoryRecord getNutritionHistory(Long userId, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);

        List<NutritionHistoryRecord.DailySummary> history = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            List<MealEntity> meals = mealRepository.findByCustomerIdAndMealTimeBetween(
                    userId,
                    date.atStartOfDay(),
                    date.atTime(23, 59, 59)
            );

            double totalCalories = meals.stream()
                    .mapToDouble(MealEntity::getTotalCalories)
                    .sum();

            history.add(new NutritionHistoryRecord.DailySummary(date, totalCalories, meals.size()));
        }

        return new NutritionHistoryRecord(history);
    }

    /**
     * Создает нового пользователя в системе
     *
     * @param dto DTO с данными пользователя
     * @return сохраненная сущность пользователя
     */
    @Override
    public CustomerEntity createUser(UserCreateRecord dto) {
        CustomerEntity user = new CustomerEntity();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setAge(dto.age());
        user.setHeight(dto.height());
        user.setWeight(dto.weight());
        user.setGender(dto.gender());
        user.setGoal(dto.goal());
        return customerRepository.save(user);
    }

    private DailyReportRecord.MealInfo mapMealToInfo(MealEntity meal) {
        return new DailyReportRecord.MealInfo(
                meal.getMealType(),
                meal.getMealTime(),
                meal.getTotalCalories(),
                meal.getDishes().stream()
                        .map(md -> new DailyReportRecord.DishInfo(
                                md.getDish().getName(),
                                md.getPortions(),
                                md.getTotalCalories()
                        )).toList()
        );
    }
}
