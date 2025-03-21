package com.theus.tt.exception.notfound;

import com.theus.tt.exception.EntityNotFoundException;
import lombok.experimental.StandardException;

@StandardException
public class DishNotFoundException extends EntityNotFoundException {
    public DishNotFoundException(Long id) {
        super("Dish not found with id: " + id);
    }
}
