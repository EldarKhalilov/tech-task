package com.theus.tt.mapper;

import com.theus.tt.dto.request.UserCreateRecord;
import com.theus.tt.entity.CustomerEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerEntity toEntity(UserCreateRecord dto) {
        if ( dto == null ) {
            return null;
        }

        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setName( dto.name() );
        customerEntity.setEmail( dto.email() );
        customerEntity.setAge( dto.age() );
        customerEntity.setHeight( dto.height() );
        customerEntity.setWeight( dto.weight() );
        customerEntity.setGender( dto.gender() );
        customerEntity.setGoal( dto.goal() );

        return customerEntity;
    }
}
