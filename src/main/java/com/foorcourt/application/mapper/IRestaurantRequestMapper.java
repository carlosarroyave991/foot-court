package com.foorcourt.application.mapper;

import com.foorcourt.application.dto.request.RestaurantRequest;
import com.foorcourt.application.dto.response.restaurant.RestaurantResponse;
import com.foorcourt.domain.model.RestaurantModel;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IRestaurantRequestMapper {
    
    @Mapping(target = "dishes", ignore = true)
    @Mapping(target = "orders", ignore = true)
    RestaurantModel toModel(RestaurantRequest request);
    
    RestaurantResponse toResponse(RestaurantModel model);
}