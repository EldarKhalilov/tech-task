package com.theus.tt.exception;

import lombok.experimental.StandardException;

@StandardException
public class CustomerAlreadyExistsException extends RuntimeException {
    private static final String MESSAGE = "Customer with these credentials already exists";

    public CustomerAlreadyExistsException() {
        super(MESSAGE);
    }
}
