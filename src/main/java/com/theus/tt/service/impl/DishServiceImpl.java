package com.theus.tt.service.impl;

import com.theus.tt.dto.request.DishCreateRecord;
import com.theus.tt.entity.DishEntity;
import com.theus.tt.exception.notfound.DishNotFoundException;
import com.theus.tt.mapper.DishMapper;
import com.theus.tt.repository.DishRepository;
import com.theus.tt.service.BaseService;
import com.theus.tt.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
        log.info("Creating dish: {}", dto.name());
        DishEntity entity = mapper.toEntity(dto);
        repository.save(entity);
        log.debug("Created dish: {}", entity.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        log.debug("Checking existence of dish ID: {}", id);
        boolean exists = repository.existsById(id);
        log.trace("Dish ID: {} exists: {}", id, exists);
        return exists;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DishEntity> findAllById(List<Long> ids) {
        log.debug("Finding dishes by IDs: {}", ids);
        List<DishEntity> result = repository.findAllById(ids);
        log.trace("Found {} dishes", result.size());
        return result;
    }
}
