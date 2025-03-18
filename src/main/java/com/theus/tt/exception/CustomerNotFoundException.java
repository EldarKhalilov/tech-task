package com.theus.tt.exception;

import lombok.experimental.StandardException;

@StandardException
public class CustomerNotFoundException extends Exception {
    private static final String MESSAGE = "Customer with these credentials not found";

    public CustomerNotFoundException() {
        super(MESSAGE);
    }
}
