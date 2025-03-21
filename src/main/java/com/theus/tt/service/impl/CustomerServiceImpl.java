package com.theus.tt.service.impl;

import com.theus.tt.dto.request.UserCreateRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.exception.CustomerAlreadyExistsException;
import com.theus.tt.exception.notfound.CustomerNotFoundException;
import com.theus.tt.mapper.CustomerMapper;
import com.theus.tt.repository.CustomerRepository;
import com.theus.tt.service.BaseService;
import com.theus.tt.service.CustomerService;
import com.theus.tt.util.CaloriesCalculatorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl extends BaseService<CustomerEntity, Long> implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CaloriesCalculatorUtil util;
    private final CustomerMapper mapper;

    protected CustomerServiceImpl(CustomerRepository customerRepository,
                                  CaloriesCalculatorUtil util,
                                  CustomerMapper mapper) {
        super(
                customerRepository,
                CustomerNotFoundException::new
        );
        this.customerRepository = customerRepository;
        this.util = util;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void createUser(UserCreateRecord dto) {
        if (customerRepository.existsByEmail(dto.email())) {
            throw new CustomerAlreadyExistsException();
        }
        repository.save(mapper.toEntity(dto));
    }

    @Override
    public double calculateDailyCalories(Long userId) {
        CustomerEntity user = getById(userId);
        return util.calculateDailyCalories(user);
    }
}
