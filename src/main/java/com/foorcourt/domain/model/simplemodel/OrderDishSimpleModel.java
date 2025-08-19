package com.foorcourt.domain.model.simplemodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDishSimpleModel {
    private Long id;
    private Integer quantity;
}
