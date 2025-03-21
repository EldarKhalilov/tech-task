package com.theus.tt.service.impl;

import com.theus.tt.dto.request.DishCreateRecord;
import com.theus.tt.entity.DishEntity;
import com.theus.tt.exception.notfound.DishNotFoundException;
import com.theus.tt.mapper.DishMapper;
import com.theus.tt.repository.DishRepository;
import com.theus.tt.service.BaseService;
import com.theus.tt.service.DishService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends BaseService<DishEntity, Long> implements DishService {
    private final DishRepository repository;
    private final DishMapper mapper;

    protected DishServiceImpl(DishRepository repository1,
                              DishMapper mapper) {
        super(repository1,
                DishNotFoundException::new);
        this.repository = repository1;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void createDish(DishCreateRecord dto) {
        repository.save(mapper.toEntity(dto));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DishEntity> findAllById(List<Long> ids) {
        return repository.findAllById(ids);
    }
}
