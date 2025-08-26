package com.foorcourt.application.handler;

import com.foorcourt.application.dto.request.restaurant.RestaurantRequest;
import com.foorcourt.application.dto.response.restaurant.RestaurantResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IRestaurantHandler {
    RestaurantResponse save(RestaurantRequest request);
    Page<RestaurantResponse> getAll(Pageable pageable);
    Optional<RestaurantResponse> findById(Long id);
}