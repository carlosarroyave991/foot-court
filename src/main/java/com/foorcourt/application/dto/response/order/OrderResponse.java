package com.foorcourt.application.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderResponse {
    private Long id;
    private LocalDate date;
    private String status;
    private Long chefId;
    private Long clientId;
    private Long restaurantId;
    private String restaurantName;
    private List<OrderDishResponse> dishes;
}