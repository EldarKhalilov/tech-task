package com.theus.tt.service;

import com.theus.tt.exception.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.function.Function;

public abstract class BaseService<T, ID> {
    protected final JpaRepository<T, ID> repository;
    private final Function<ID, ? extends EntityNotFoundException> exceptionCreator;

    protected BaseService(
            JpaRepository<T, ID> repository,
            Function<ID, ? extends EntityNotFoundException> exceptionCreator
    ) {
        this.repository = repository;
        this.exceptionCreator = exceptionCreator;
    }

    public T getById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> exceptionCreator.apply(id));
    }
}
