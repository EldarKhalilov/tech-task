package com.theus.tt.service.impl;

import com.theus.tt.dto.request.UserCreateRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.exception.CustomerAlreadyExistsException;
import com.theus.tt.exception.CustomerNotFoundException;
import com.theus.tt.repository.CustomerRepository;
import com.theus.tt.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public void createUser(UserCreateRecord dto) throws CustomerAlreadyExistsException {
        if (customerRepository.existsByEmail(dto.email())) {
            throw new CustomerAlreadyExistsException();
        }

        var user = new CustomerEntity();
        user.setEmail(dto.email());
        user.setName(dto.name());
        user.setAge(dto.age());
        user.setHeight(dto.height());
        user.setWeight(dto.weight());
        user.setGender(dto.gender());
        user.setGoal(dto.goal());

        customerRepository.save(user);
    }

    @Override
    public double calculateDailyCalories(Long userId) throws CustomerNotFoundException {
        CustomerEntity user = getById(userId);
        return user.calculateDailyCalories();
    }

    @Override
    public CustomerEntity getById(Long id) throws CustomerNotFoundException {
        return customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);
    }
}
