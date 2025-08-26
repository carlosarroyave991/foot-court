package com.foorcourt.application.mapper;

import com.foorcourt.application.dto.request.order.OrderDishRequest;
import com.foorcourt.application.dto.request.order.OrderRequest;
import com.foorcourt.application.dto.response.order.OrderDishResponse;
import com.foorcourt.application.dto.response.order.OrderResponse;
import com.foorcourt.domain.model.OrderModel;
import com.foorcourt.domain.model.simplemodel.OrderDishSimpleModel;
import org.mapstruct.*;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface IOrderDtoMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "chefId", ignore = true)
    @Mapping(target = "restaurant.id", source = "restaurantId")
    @Mapping(target = "ordersDishes", source = "dishes")
    OrderModel toModel(OrderRequest request);
    
    @Mapping(target = "restaurantId", source = "restaurant.id")
    @Mapping(target = "restaurantName", source = "restaurant.name")
    @Mapping(target = "dishes", source = "ordersDishes")
    OrderResponse toResponse(OrderModel model);
    
    @Mapping(target = "id", source = "dishId")
    OrderDishSimpleModel toOrderDishModel(OrderDishRequest request);
    
    @Mapping(target = "dishId", source = "id")
    OrderDishResponse toOrderDishResponse(OrderDishSimpleModel model);
}