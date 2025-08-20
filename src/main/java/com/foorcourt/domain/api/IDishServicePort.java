package com.foorcourt.domain.api;

import com.foorcourt.domain.model.DishModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDishServicePort {
    DishModel save(DishModel model);
    DishModel update(DishModel model);
    DishModel changeStatusDish(DishModel model);
    Page<DishModel> findByRestaurantIdAndCategoryId(Pageable pageable, Long restaurantId, Long categoryId);
}
