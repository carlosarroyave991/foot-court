package com.foorcourt.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foorcourt.domain.model.simplemodel.DishSimpleModel;
import com.foorcourt.infraestructure.out.jpa.entity.DishEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModel {
    private Long id;
    private String name;
    private String description;

    @JsonIgnore
    private List<DishSimpleModel> dishes;
}
