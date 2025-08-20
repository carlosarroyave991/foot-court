package com.foorcourt.infraestructure.out.jpa.mapper;

import com.foorcourt.domain.model.DishModel;
import com.foorcourt.infraestructure.out.jpa.entity.DishEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IDishEntityMapper {
    DishEntity toEntity(DishModel model);

    @InheritInverseConfiguration
    DishModel toModel(DishEntity entity);
}
