package com.foorcourt.infraestructure.out.jpa.adapter;

import com.foorcourt.domain.model.CategoryModel;
import com.foorcourt.domain.spi.ICategoryPersistencePort;
import com.foorcourt.infraestructure.out.jpa.entity.CategoryEntity;
import com.foorcourt.infraestructure.out.jpa.mapper.ICategoryEntityMapper;
import com.foorcourt.infraestructure.out.jpa.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {
    private final ICategoryEntityMapper mapper;
    private final ICategoryRepository repository;

    @Override
    public Optional<CategoryModel> findById(Long id) {
        Optional<CategoryEntity> entity = repository.findById(id);
        return entity.map(mapper::toModel);
    }
}
