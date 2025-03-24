package com.theus.tt.exception.notfound;

import lombok.experimental.StandardException;

@StandardException
public class DishNotFoundException extends Exception {
    private static final String MESSAGE = "Dish not found";

    public DishNotFoundException() {
        super(MESSAGE);
    }
}
