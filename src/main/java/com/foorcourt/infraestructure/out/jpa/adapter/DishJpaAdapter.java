package com.foorcourt.infraestructure.out.jpa.adapter;

import com.foorcourt.domain.model.DishModel;
import com.foorcourt.domain.spi.IDishPersistencePort;
import com.foorcourt.infraestructure.out.jpa.entity.DishEntity;
import com.foorcourt.infraestructure.out.jpa.mapper.IDishEntityMapper;
import com.foorcourt.infraestructure.out.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {
    private final IDishRepository repository;
    private final IDishEntityMapper mapper;

    @Override
    public DishModel save(DishModel model) {
        DishEntity entity = mapper.toEntity(model);
        DishEntity savedEntity = repository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<DishModel> findById(Long id) {
        Optional<DishEntity> entity = repository.findById(id);
        return entity.map(mapper::toModel);
    }

    @Override
    public Page<DishModel> findByRestaurantIdAndCategoryId(Pageable pageable, Long restaurantId, Long categoryId) {
        Page<DishEntity> dishEntities = repository.findByRestaurantIdAndCategoryId(restaurantId, categoryId, pageable);
        return dishEntities.map(mapper::toModel);
    }

    @Override
    public DishModel changeStatusDish(DishModel model) {
        DishEntity entity = mapper.toEntity(model);
        DishEntity savedEntity = repository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Transactional
    @Override
    public DishModel update(DishModel model) {
        DishEntity entity = mapper.toEntity(model);
        DishEntity savedEntity = repository.save(entity);
        return mapper.toModel(savedEntity);
    }
}