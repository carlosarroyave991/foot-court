package com.foorcourt.application.dto.response.dish;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishSimpleResponse {
    private String name;
    private String description;
    private BigDecimal price;
    private String urlImage;
}
