package com.foorcourt.application.handler.impl;

import com.foorcourt.application.dto.request.restaurant.RestaurantRequest;
import com.foorcourt.application.dto.response.restaurant.RestaurantResponse;
import com.foorcourt.application.handler.IRestaurantHandler;
import com.foorcourt.application.mapper.IRestaurantDtoMapper;
import com.foorcourt.domain.api.IRestaurantServicePort;
import com.foorcourt.domain.model.RestaurantModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {
    
    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantDtoMapper restaurantRequestMapper;
    
    @Override
    public RestaurantResponse save(RestaurantRequest request) {
        RestaurantModel model = restaurantRequestMapper.toModel(request);
        RestaurantModel savedModel = restaurantServicePort.save(model);
        return restaurantRequestMapper.toResponse(savedModel);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantResponse> getAll(Pageable pageable) {
        Page<RestaurantModel> models = restaurantServicePort.getAll(pageable);
        return models.map(restaurantRequestMapper::toResponse);
    }

    @Override
    public Optional<RestaurantResponse> findById(Long id) {
        Optional<RestaurantModel> restaurantModel = restaurantServicePort.findById(id);
        return restaurantModel.map(restaurantRequestMapper::toResponse);
    }
}