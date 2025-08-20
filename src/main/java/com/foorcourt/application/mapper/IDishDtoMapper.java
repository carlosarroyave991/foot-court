package com.foorcourt.application.mapper;

import com.foorcourt.application.dto.request.dish.DishChangeStatusRequest;
import com.foorcourt.application.dto.request.dish.DishRequest;
import com.foorcourt.application.dto.request.dish.DishUpdateRequest;
import com.foorcourt.application.dto.response.dish.DishResponse;
import com.foorcourt.application.dto.response.dish.DishSimpleResponse;
import com.foorcourt.domain.model.DishModel;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IDishDtoMapper {
    
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "ordersDishes", ignore = true)
    DishModel toModel(DishRequest request);

    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "ordersDishes", ignore = true)
    DishModel toModelUpdate(DishUpdateRequest request);

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "urlImage", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "ordersDishes", ignore = true)
    DishModel toModelEnableDisable(DishChangeStatusRequest request);

    DishResponse toResponse(DishModel model);
    
    DishSimpleResponse toDishSimpleResponse(DishModel model);
}