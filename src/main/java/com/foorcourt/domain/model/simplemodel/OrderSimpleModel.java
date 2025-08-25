package com.foorcourt.domain.model.simplemodel;

import com.foorcourt.infraestructure.out.jpa.entity.OrderDishEntity;
import com.foorcourt.infraestructure.out.jpa.entity.RestaurantEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderSimpleModel {
    private Long id;
    private LocalDate date;
    private String status;
    private String securityCode;
    private Long chefId;
    private Long clientId;
}
