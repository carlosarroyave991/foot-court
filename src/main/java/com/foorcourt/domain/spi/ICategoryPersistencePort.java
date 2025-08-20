package com.foorcourt.domain.spi;

import com.foorcourt.domain.model.CategoryModel;

import java.util.Optional;

public interface ICategoryPersistencePort {
    Optional<CategoryModel> findById(Long id);
}
