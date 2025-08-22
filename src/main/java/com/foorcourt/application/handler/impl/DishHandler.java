package com.foorcourt.application.handler.impl;

import com.foorcourt.application.dto.request.dish.DishChangeStatusRequest;
import com.foorcourt.application.dto.request.dish.DishRequest;
import com.foorcourt.application.dto.request.dish.DishUpdateRequest;
import com.foorcourt.application.dto.response.dish.DishResponse;
import com.foorcourt.application.dto.response.dish.DishSimpleResponse;
import com.foorcourt.application.handler.IDishHandler;
import com.foorcourt.application.mapper.IDishDtoMapper;
import com.foorcourt.domain.api.IDishServicePort;
import com.foorcourt.domain.model.DishModel;
import com.foorcourt.domain.model.simplemodel.CategorySimpleModel;
import com.foorcourt.domain.model.simplemodel.RestaurantSimpleModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {
    
    private final IDishServicePort dishServicePort;
    private final IDishDtoMapper dishDtoMapper;
    
    @Override
    public DishResponse save(DishRequest request, Long restaurantId, Long categoryId) {
        DishModel model = dishDtoMapper.toModel(request);
        
        // Establecer restaurant y category
        RestaurantSimpleModel restaurant = new RestaurantSimpleModel();
        restaurant.setId(restaurantId);
        model.setRestaurant(restaurant);
        
        CategorySimpleModel category = new CategorySimpleModel();
        category.setId(categoryId);
        model.setCategory(category);
        
        DishModel savedModel = dishServicePort.save(model);
        return dishDtoMapper.toResponse(savedModel);
    }

    @Override
    public DishResponse update(DishUpdateRequest request) {
        DishModel model = dishDtoMapper.toModelUpdate(request);

        /*// Establecer restaurant y category
        RestaurantSimpleModel restaurant = new RestaurantSimpleModel();
        restaurant.setId(restaurantId);
        model.setRestaurant(restaurant);

        CategorySimpleModel category = new CategorySimpleModel();
        category.setId(categoryId);
        model.setCategory(category);*/

        DishModel updatedModel = dishServicePort.update(model);
        return dishDtoMapper.toResponse(updatedModel);
    }

    @Override
    public DishResponse changeStatusDish(DishChangeStatusRequest request) {
        DishModel model = dishDtoMapper.toModelEnableDisable(request);
        DishModel updatedModel = dishServicePort.changeStatusDish(model);
        return dishDtoMapper.toResponse(updatedModel);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<DishSimpleResponse> findAllDishesByRestaurantId(Pageable pageable, Long restaurantId, Long categoryId) {
        Page<DishModel> models = dishServicePort.findAllDishesByRestaurantId(pageable, restaurantId, categoryId);
        return models.map(dishDtoMapper::toDishSimpleResponse);
    }
}