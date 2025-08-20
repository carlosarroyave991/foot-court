package com.foorcourt.domain.spi;

import com.foorcourt.domain.model.RestaurantModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IRestaurantPersistencePort {
    void save(RestaurantModel model);
    Optional<RestaurantModel> findByNit(String nit);
    Page<RestaurantModel> findAll(Pageable pageable);
    Optional<RestaurantModel> findById(Long id);
}
