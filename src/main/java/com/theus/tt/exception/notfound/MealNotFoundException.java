package com.theus.tt.exception.notfound;

import lombok.experimental.StandardException;

@StandardException
public class MealNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Meal not found";

    public MealNotFoundException() {
        super(MESSAGE);
    }
}
