package com.foorcourt.infraestructure.out.jpa.mapper;

import com.foorcourt.domain.model.CategoryModel;
import com.foorcourt.infraestructure.out.jpa.entity.CategoryEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ICategoryEntityMapper {
    CategoryEntity toEntity(CategoryModel model);

    @InheritInverseConfiguration
    CategoryModel toModel(CategoryEntity entity);
}
