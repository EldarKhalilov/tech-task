package com.theus.tt.exception;

import lombok.experimental.StandardException;

@StandardException
public class DishNotFoundException extends Exception {
    private static final String MESSAGE = "Dish with these credentials not found";

    public DishNotFoundException() {
        super(MESSAGE);
    }
}
