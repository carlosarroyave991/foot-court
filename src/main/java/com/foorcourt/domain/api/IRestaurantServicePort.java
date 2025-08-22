package com.foorcourt.domain.api;

import com.foorcourt.domain.model.RestaurantModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IRestaurantServicePort {
    RestaurantModel save(RestaurantModel model);
    Page<RestaurantModel> getAll(Pageable pageable);
    Optional<RestaurantModel> findById(Long id);
}
