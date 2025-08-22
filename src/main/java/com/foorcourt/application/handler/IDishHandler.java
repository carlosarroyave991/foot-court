package com.foorcourt.application.handler;

import com.foorcourt.application.dto.request.dish.DishChangeStatusRequest;
import com.foorcourt.application.dto.request.dish.DishRequest;
import com.foorcourt.application.dto.request.dish.DishUpdateRequest;
import com.foorcourt.application.dto.response.dish.DishResponse;
import com.foorcourt.application.dto.response.dish.DishSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDishHandler {
    DishResponse save(DishRequest request, Long restaurantId, Long categoryId);
    DishResponse update(DishUpdateRequest request);
    DishResponse changeStatusDish(DishChangeStatusRequest request);
    Page<DishSimpleResponse> findAllDishesByRestaurantId(Pageable pageable, Long restaurantId, Long categoryId);
}