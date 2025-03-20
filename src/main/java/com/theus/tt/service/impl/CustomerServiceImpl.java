package com.theus.tt.service.impl;

import com.theus.tt.dto.request.UserCreateRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.exception.CustomerAlreadyExistsException;
import com.theus.tt.exception.CustomerNotFoundException;
import com.theus.tt.mapper.CustomerMapper;
import com.theus.tt.repository.CustomerRepository;
import com.theus.tt.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    public void createUser(UserCreateRecord dto) throws CustomerAlreadyExistsException {
        if (repository.existsByEmail(dto.email())) {
            throw new CustomerAlreadyExistsException();
        }

        CustomerEntity user = mapper.toEntity(dto);
        repository.save(user);
    }

    @Override
    public double calculateDailyCalories(Long userId) throws CustomerNotFoundException {
        CustomerEntity user = getById(userId);
        return user.calculateDailyCalories();
    }

    @Override
    public CustomerEntity getById(Long id) throws CustomerNotFoundException {
        return repository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);
    }
}
