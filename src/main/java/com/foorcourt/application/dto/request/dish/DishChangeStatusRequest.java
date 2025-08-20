package com.foorcourt.application.dto.request.dish;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishChangeStatusRequest {
    @NotNull(message = "The name field cannot be empty.")
    private Long id;

    @NotNull(message = "The userId field cannot be empty.")
    private Long userId;

    @NotNull(message = "The active field cannot be empty.")
    private Boolean active;
}
