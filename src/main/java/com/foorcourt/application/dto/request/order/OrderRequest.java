package com.foorcourt.application.dto.request.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRequest {
    
    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;
    
    @NotNull(message = "Client ID is required")
    private Long clientId;
    
    @NotEmpty(message = "Order must have at least one dish")
    @Valid
    private List<OrderDishRequest> dishes;
}