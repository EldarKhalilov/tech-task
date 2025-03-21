package com.theus.tt.exception.notfound;

import com.theus.tt.exception.EntityNotFoundException;

public class MealNotFoundException extends EntityNotFoundException {
    public MealNotFoundException(Long id) {
        super("Meal not found with id: " + id);
    }
}
