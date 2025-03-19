package com.theus.tt.service;

import com.theus.tt.dto.request.UserCreateRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.exception.CustomerAlreadyExistsException;
import com.theus.tt.exception.CustomerNotFoundException;

public interface CustomerService {
    void createUser(UserCreateRecord dto) throws CustomerAlreadyExistsException;
    double calculateDailyCalories(Long userId) throws CustomerNotFoundException;
    CustomerEntity getById(Long id) throws CustomerNotFoundException;
}
