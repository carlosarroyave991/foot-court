package com.foorcourt.infraestructure.out.jpa.mapper;

import com.foorcourt.domain.model.RestaurantModel;
import com.foorcourt.infraestructure.out.jpa.entity.RestaurantEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IRestaurantEntityMapper {
    
    @Mapping(target = "orders.restaurant", ignore = true)
    @Mapping(target = "orders.ordersDishes", ignore = true)
    @Mapping(target = "dishes.category", ignore = true)
    @Mapping(target = "dishes.restaurant", ignore = true)
    @Mapping(target = "dishes.ordersDishes", ignore = true)
    RestaurantEntity toEntity(RestaurantModel model);

    @InheritInverseConfiguration
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "dishes", ignore = true)
    RestaurantModel toModel(RestaurantEntity entity);
}
