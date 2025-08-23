package com.foorcourt.infraestructure.out.jpa.mapper;

import com.foorcourt.domain.model.OrderModel;
import com.foorcourt.domain.model.simplemodel.OrderDishSimpleModel;
import com.foorcourt.infraestructure.out.jpa.entity.OrderDishEntity;
import com.foorcourt.infraestructure.out.jpa.entity.OrderEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {IRestaurantEntityMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderEntityMapper {
    
    OrderModel toModel(OrderEntity entity);
    
    OrderEntity toEntity(OrderModel model);
    
    @Mapping(target = "id", source = "dish.id")
    @Mapping(target = "dishName", ignore = true)
    OrderDishSimpleModel toOrderDishSimpleModel(OrderDishEntity entity);
    
    @Mapping(target = "dish.id", source = "id")
    @Mapping(target = "order", ignore = true)
    OrderDishEntity toOrderDishEntity(OrderDishSimpleModel model);
}