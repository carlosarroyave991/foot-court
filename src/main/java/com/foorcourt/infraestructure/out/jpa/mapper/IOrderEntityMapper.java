package com.foorcourt.infraestructure.out.jpa.mapper;

import com.foorcourt.domain.model.OrderModel;
import com.foorcourt.domain.model.simplemodel.OrderDishSimpleModel;
import com.foorcourt.infraestructure.out.jpa.entity.DishEntity;
import com.foorcourt.infraestructure.out.jpa.entity.OrderDishEntity;
import com.foorcourt.infraestructure.out.jpa.entity.OrderEntity;
import com.foorcourt.infraestructure.out.jpa.entity.RestaurantEntity;

import java.util.List;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {IRestaurantEntityMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderEntityMapper {
    
    OrderModel toModel(OrderEntity entity);
    
    @Mapping(target = "ordersDishes", ignore = true)
    OrderEntity toEntity(OrderModel model);
    
    @Mapping(target = "id", source = "dish.id")
    @Mapping(target = "dishName", ignore = true)
    OrderDishSimpleModel toOrderDishSimpleModel(OrderDishEntity entity);
    
    @AfterMapping
    default void setOrderDishes(@MappingTarget OrderEntity entity, OrderModel model) {
        if (model.getOrdersDishes() != null && !model.getOrdersDishes().isEmpty()) {
            List<OrderDishEntity> orderDishes = model.getOrdersDishes().stream()
                .map(orderDish -> {
                    OrderDishEntity orderDishEntity = new OrderDishEntity();
                    orderDishEntity.setQuantity(orderDish.getQuantity());
                    orderDishEntity.setOrder(entity);
                    
                    DishEntity dish = new DishEntity();
                    dish.setId(orderDish.getId());
                    orderDishEntity.setDish(dish);
                    
                    return orderDishEntity;
                }).toList();
            entity.setOrdersDishes(orderDishes);
        }
    }
}