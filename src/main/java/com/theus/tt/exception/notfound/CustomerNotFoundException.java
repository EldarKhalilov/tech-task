package com.theus.tt.exception.notfound;

import lombok.experimental.StandardException;

@StandardException
public class CustomerNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Customer not found";

    public CustomerNotFoundException() {
        super(MESSAGE);
    }
}
