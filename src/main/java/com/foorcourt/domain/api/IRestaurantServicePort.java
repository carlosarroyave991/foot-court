package com.foorcourt.domain.api;

import com.foorcourt.domain.model.RestaurantModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRestaurantServicePort {
    RestaurantModel save(RestaurantModel model);
    Page<RestaurantModel> getAll(Pageable pageable);
}
