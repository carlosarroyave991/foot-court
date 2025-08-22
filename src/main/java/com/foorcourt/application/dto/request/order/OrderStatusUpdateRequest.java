package com.foorcourt.application.dto.request.order;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderStatusUpdateRequest {
    
    @NotBlank(message = "Status is required")
    private String status;
}