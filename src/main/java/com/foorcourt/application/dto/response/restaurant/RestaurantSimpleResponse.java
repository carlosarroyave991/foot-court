package com.foorcourt.application.dto.response.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RestaurantSimpleResponse {
    private String name;
    private String address;
    private String phone;
    private String logoUrl;
}
