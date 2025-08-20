package com.foorcourt.application.dto.request.restaurant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRequest {
    @Null(message = "The id must be null for a create operation")
    private Long id;

    @NotNull(message = "The name field cannot be empty.")
    @Pattern(regexp = "^(?!\\d+$).*$", message = "Name cannot contain only numbers.")
    private String name;
    @NotNull(message = "The address field cannot be empty.")
    private String address;
    @NotNull(message = "The ownerId field cannot be empty.")
    private Long ownerId;
    @NotNull(message = "The phone field cannot be empty.")
    private String phone;
    @NotNull(message = "The logoUrl field cannot be empty.")
    private String logoUrl;
    @NotNull(message = "The nit field cannot be empty.")
    private String nit;
}
