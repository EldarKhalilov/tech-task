package com.theus.tt.service.impl;

import com.theus.tt.dto.request.UserCreateRecord;
import com.theus.tt.entity.CustomerEntity;
import com.theus.tt.entity.enums.GenderEnum;
import com.theus.tt.entity.enums.GoalEnum;
import com.theus.tt.exception.CustomerAlreadyExistsException;
import com.theus.tt.exception.notfound.CustomerNotFoundException;
import com.theus.tt.mapper.CustomerMapper;
import com.theus.tt.repository.CustomerRepository;
import com.theus.tt.service.impl.CustomerServiceImpl;
import com.theus.tt.util.CaloriesCalculatorUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CaloriesCalculatorUtil util;

    @Mock
    private CustomerMapper mapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    public void createUser_ShouldCreateUser() throws Exception {
        UserCreateRecord dto = new UserCreateRecord(
                "John Doe",
                "test@mail.ru",
                (byte) 30,
                170.0,
                (short) 70,
                GenderEnum.MALE,
                GoalEnum.MAINTENANCE
        );

        CustomerEntity entity = new CustomerEntity();
        when(repository.existsByEmail(dto.email())).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(entity);

        customerService.createUser(dto);

        verify(repository).save(entity);
    }

    @Test
    public void createUser_ShouldThrowExceptionWhenEmailExists() {
        UserCreateRecord dto = new UserCreateRecord(
                "John Doe",
                "test@mail.ru",
                (byte) 30,
                170.0,
                (short) 70,
                GenderEnum.MALE,
                GoalEnum.MAINTENANCE
        );

        when(repository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(CustomerAlreadyExistsException.class,
                () -> customerService.createUser(dto));
    }

    @Test
    public void calculateDailyCalories_ShouldReturnValue() throws Exception {
        Long userId = 1L;
        CustomerEntity user = mock(CustomerEntity.class);
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(util.calculateDailyCalories(user)).thenReturn(2000.0);

        double result = customerService.calculateDailyCalories(userId);

        assertThat(result).isEqualTo(2000.0);
    }

    @Test
    void calculateDailyCalories_ShouldThrowException() {
        Long invalidUserId = 999L;
        when(repository.findById(invalidUserId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> customerService.calculateDailyCalories(invalidUserId));
    }

    @Test
    void getById_ShouldReturnUser() throws Exception {
        Long userId = 1L;
        CustomerEntity user = new CustomerEntity();
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        CustomerEntity result = customerService.getById(userId);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void getById_ShouldThrowException() {
        Long invalidUserId = 999L;
        when(repository.findById(invalidUserId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> customerService.getById(invalidUserId));
    }
}
