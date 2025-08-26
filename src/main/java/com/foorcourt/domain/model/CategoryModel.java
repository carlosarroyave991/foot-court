package com.foorcourt.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foorcourt.domain.model.simplemodel.DishSimpleModel;
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
