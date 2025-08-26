package com.foorcourt.domain.model;

import com.foorcourt.domain.model.simplemodel.OrderDishSimpleModel;
import com.foorcourt.domain.model.simplemodel.RestaurantSimpleModel;
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
public class OrderModel {
    private Long id;
    private LocalDate date;
    private String status;
    private String securityCode;
    private Long chefId;
    private Long clientId;
    private RestaurantSimpleModel restaurant;
    private List<OrderDishSimpleModel> ordersDishes;
}
