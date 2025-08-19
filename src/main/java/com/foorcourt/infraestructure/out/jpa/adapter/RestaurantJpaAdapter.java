package com.foorcourt.infraestructure.out.jpa.adapter;

import com.foorcourt.domain.model.RestaurantModel;
import com.foorcourt.domain.spi.IRestaurantPersistencePort;
import com.foorcourt.infraestructure.out.jpa.entity.RestaurantEntity;
import com.foorcourt.infraestructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.foorcourt.infraestructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {
    private final IRestaurantRepository repository;
    private final IRestaurantEntityMapper mapper;

    @Override
    public Page<RestaurantModel> findAll(Pageable pageable) {
        Page<RestaurantEntity> entities = repository.findAll(pageable);
        return entities.map(mapper::toModel);
    }

    @Override
    public Optional<RestaurantModel> findByNit(String nit) {
        Optional<RestaurantEntity> entity = repository.findByNit(nit);
        return entity.map(mapper::toModel);
    }

    @Transactional
    @Override
    public void save(RestaurantModel model) {
        RestaurantEntity entity = mapper.toEntity(model);
        repository.save(entity);
    }
}
