package com.foorcourt.domain.model;

import com.foorcourt.domain.model.simplemodel.DishSimpleModel;
import com.foorcourt.domain.model.simplemodel.OrderSimpleModel;
import com.foorcourt.infraestructure.out.jpa.entity.DishEntity;
import com.foorcourt.infraestructure.out.jpa.entity.OrderEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDishModel {
    private Long id;
    private Integer quantity;
    private OrderSimpleModel order;
    private DishSimpleModel dish;
}
