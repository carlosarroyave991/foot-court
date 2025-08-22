package com.foorcourt.application.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDishResponse {
    private Long dishId;
    private String dishName;
    private Integer quantity;
}