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
    RestaurantEntity toEntity(RestaurantModel model);

    @InheritInverseConfiguration
    RestaurantModel toModel(RestaurantEntity entity);
}
