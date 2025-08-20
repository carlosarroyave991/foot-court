package com.foorcourt.application.dto.request.dish;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishRequest {
    @Null(message = "The id must be null for a create operation")
    private Long id;

    @NotNull(message = "The name field cannot be empty.")
    @Pattern(regexp = "^(?!\\d+$).*$", message = "Name cannot contain only numbers.")
    private String name;
    @NotNull(message = "The description field cannot be empty.")
    private String description;
    @NotNull(message = "The price field cannot be empty.")
    @DecimalMin(value = "0.0", inclusive = false, message = "The price must be greater than 0")
    private BigDecimal price;
    @NotNull(message = "The urlImage field cannot be empty.")
    private String urlImage;
    @NotNull(message = "The active field cannot be empty.")
    private Boolean active;
}
