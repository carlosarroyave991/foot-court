package com.foorcourt.domain.model;

import com.foorcourt.domain.model.simplemodel.CategorySimpleModel;
import com.foorcourt.domain.model.simplemodel.OrderDishSimpleModel;
import com.foorcourt.domain.model.simplemodel.RestaurantSimpleModel;
import com.foorcourt.infraestructure.out.jpa.entity.CategoryEntity;
import com.foorcourt.infraestructure.out.jpa.entity.OrderDishEntity;
import com.foorcourt.infraestructure.out.jpa.entity.RestaurantEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishModel {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String urlImage;
    private Boolean active;

    private CategorySimpleModel category;
    private RestaurantSimpleModel restaurant;
    private List<OrderDishSimpleModel> ordersDishes;
}
