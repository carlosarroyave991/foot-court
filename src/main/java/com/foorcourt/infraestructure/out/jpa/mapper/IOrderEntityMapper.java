package com.foorcourt.infraestructure.out.jpa.mapper;

import com.foorcourt.domain.model.OrderModel;
import com.foorcourt.infraestructure.out.jpa.entity.OrderEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IOrderEntityMapper {
    
    @Mapping(target = "restaurant.dishes", ignore = true)
    @Mapping(target = "restaurant.orders", ignore = true)
    @Mapping(target = "ordersDishes", ignore = true)
    OrderEntity toEntity(OrderModel model);

    @Mapping(target = "ordersDishes", ignore = true)
    OrderModel toModel(OrderEntity entity);
}