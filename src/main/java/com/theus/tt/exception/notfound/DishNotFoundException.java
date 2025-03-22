package com.theus.tt.exception.notfound;

import lombok.experimental.StandardException;

@StandardException
public class DishNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Dish not found";

    public DishNotFoundException() {
        super(MESSAGE);
    }
}
