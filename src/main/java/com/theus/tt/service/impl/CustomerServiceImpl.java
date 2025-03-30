package com.theus.tt.service.impl;

import com.theus.tt.dto.request.UserCreateRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.exception.CustomerAlreadyExistsException;
import com.theus.tt.exception.notfound.CustomerNotFoundException;
import com.theus.tt.mapper.CustomerMapper;
import com.theus.tt.repository.CustomerRepository;
import com.theus.tt.service.CustomerService;
import com.theus.tt.util.CaloriesCalculatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CaloriesCalculatorUtil util;
    private final CustomerMapper mapper;

    @Override
    @Transactional
    public void createUser(UserCreateRecord dto) throws CustomerAlreadyExistsException {
        log.info("Creating user with name: {}", dto.name());
        if (customerRepository.existsByEmail(dto.email())) {
            log.warn("User with email **** already exists");
            throw new CustomerAlreadyExistsException();
        }
        CustomerEntity entity = mapper.toEntity(dto);
        customerRepository.save(entity);
        log.debug("Created user: {}", entity.getName());
    }

    @Override
    public double calculateDailyCalories(Long userId) throws CustomerNotFoundException {
        log.debug("Calculating daily calories for user");
        CustomerEntity user = getById(userId);
        double calories = util.calculateDailyCalories(user);
        log.info("Calculated daily calories: {}", calories);
        return calories;
    }

    @Override
    public CustomerEntity getById(Long id) throws CustomerNotFoundException {
        return customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);
    }
}
