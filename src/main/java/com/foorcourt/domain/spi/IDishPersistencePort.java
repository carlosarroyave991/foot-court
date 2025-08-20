package com.foorcourt.domain.spi;

import com.foorcourt.domain.model.DishModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IDishPersistencePort {
    DishModel save(DishModel model);
    DishModel update(DishModel model);
    DishModel changeStatusDish(DishModel model);
    Optional<DishModel> findById(Long id);
    Page<DishModel> findByRestaurantIdAndCategoryId(Pageable pageable, Long restaurantId, Long categoryId);
}
