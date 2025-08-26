package com.foorcourt.domain.util;

import com.foorcourt.domain.exception.ValidationException;
import com.foorcourt.domain.model.DishModel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DishValidationService {

    public void validateDishData(DishModel model) {
        if (model.getName() == null || model.getName().trim().isEmpty()) {
            throw new ValidationException("Dish name is required");
        }
        if (model.getDescription() == null || model.getDescription().trim().isEmpty()) {
            throw new ValidationException("Dish description is required");
        }
        if (model.getPrice() == null || model.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Dish price must be greater than zero");
        }
        if (model.getUrlImage() == null || model.getUrlImage().trim().isEmpty()) {
            throw new ValidationException("Dish image URL is required");
        }
        if (model.getRestaurant() == null || model.getRestaurant().getId() == null) {
            throw new ValidationException("Restaurant is required");
        }
        if (model.getCategory() == null || model.getCategory().getId() == null) {
            throw new ValidationException("Category is required");
        }
    }
}
