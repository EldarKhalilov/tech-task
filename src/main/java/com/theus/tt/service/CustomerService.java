package com.theus.tt.service;

import com.theus.tt.dto.request.UserCreateRecord;
import com.theus.tt.entity.CustomerEntity;

public interface CustomerService {
    void createUser(UserCreateRecord dto);
    double calculateDailyCalories(Long userId);
    CustomerEntity getById(Long id);
}
