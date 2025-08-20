package com.foorcourt.domain.usecase;

import com.foorcourt.domain.api.IDishServicePort;
import com.foorcourt.domain.exception.BusinessException;
import com.foorcourt.domain.exception.NotFoundException;
import com.foorcourt.domain.exception.ValidationException;
import com.foorcourt.domain.model.CategoryModel;
import com.foorcourt.domain.model.DishModel;
import com.foorcourt.domain.model.RestaurantModel;
import com.foorcourt.domain.model.simplemodel.CategorySimpleModel;
import com.foorcourt.domain.model.simplemodel.RestaurantSimpleModel;
import com.foorcourt.domain.spi.ICategoryPersistencePort;
import com.foorcourt.domain.spi.IDishPersistencePort;
import com.foorcourt.domain.spi.IRestaurantPersistencePort;
import com.foorcourt.domain.util.DishValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

import static com.foorcourt.domain.exception.error.CommonErrorCode.*;

/**
 * Clase usada para implementar la logica de negocio sobre cada funcion
 */
@RequiredArgsConstructor
public class DishUseCase implements IDishServicePort {
    private final IDishPersistencePort dishService;
    private final IRestaurantPersistencePort restaurantService;
    private final ICategoryPersistencePort categoryService;
    private final DishValidationService dishValidationService;

    @Override
    public DishModel save(DishModel model) {
        dishValidationService.validateDishData(model);
        
        Optional<RestaurantModel> restaurantExisting = restaurantService.findById(model.getRestaurant().getId());
        if (restaurantExisting.isEmpty()) throw new NotFoundException(RESTAURANT_NOT_FOUND);

        Optional<CategoryModel> categoryExisting = categoryService.findById(model.getCategory().getId());
        if (categoryExisting.isEmpty()) throw new NotFoundException(CATEGORY_NOT_FOUND);

        // Convertir a modelos simples
        RestaurantSimpleModel restaurantSimple = new RestaurantSimpleModel();
        restaurantSimple.setId(restaurantExisting.get().getId());
        restaurantSimple.setName(restaurantExisting.get().getName());
        
        CategorySimpleModel categorySimple = new CategorySimpleModel();
        categorySimple.setId(categoryExisting.get().getId());
        categorySimple.setName(categoryExisting.get().getName());
        
        model.setRestaurant(restaurantSimple);
        model.setCategory(categorySimple);
        model.setActive(true);

        return dishService.save(model);
    }

    @Override
    public DishModel update(DishModel model) {
        if (model.getId() == null) throw new ValidationException(INVALID_ID);
        
        Optional<DishModel> existingDish = dishService.findById(model.getId());
        if (existingDish.isEmpty()) throw new NotFoundException(ID_NOT_FOUND);

        dishValidationService.validateDishData(model);
        
        // Mantener el estado activo original
        model.setActive(existingDish.get().getActive());
        
        return dishService.save(model);
    }

    @Override
    public DishModel changeStatusDish(DishModel model) {
        if (model.getId() == null) throw new ValidationException(INVALID_ID);
        
        Optional<DishModel> existingDish = dishService.findById(model.getId());
        if (existingDish.isEmpty()) throw new NotFoundException(ID_NOT_FOUND);
        
        DishModel dishToUpdate = existingDish.get();
        dishToUpdate.setActive(model.getActive());
        
        return dishService.save(dishToUpdate);
    }

    @Override
    public Page<DishModel> findByRestaurantIdAndCategoryId(Pageable pageable, Long restaurantId, Long categoryId) {
        if (restaurantId == null) throw new ValidationException(INVALID_ID);
        
        Optional<RestaurantModel> restaurant = restaurantService.findById(restaurantId);
        if (restaurant.isEmpty()) throw new NotFoundException(RESTAURANT_NOT_FOUND);
        
        if (categoryId != null) {
            Optional<CategoryModel> category = categoryService.findById(categoryId);
            if (category.isEmpty()) throw new NotFoundException(CATEGORY_NOT_FOUND);
        }
        
        return dishService.findByRestaurantIdAndCategoryId(pageable, restaurantId, categoryId);
    }

}