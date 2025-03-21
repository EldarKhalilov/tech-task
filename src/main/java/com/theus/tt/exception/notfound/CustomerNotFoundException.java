package com.theus.tt.exception.notfound;

import com.theus.tt.exception.EntityNotFoundException;
import lombok.experimental.StandardException;

@StandardException
public class CustomerNotFoundException extends EntityNotFoundException {
    public CustomerNotFoundException(Long id) {
        super("Customer not found with id: " + id);
    }
}
