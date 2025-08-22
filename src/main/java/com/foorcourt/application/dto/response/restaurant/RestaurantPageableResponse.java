package com.foorcourt.application.dto.response.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantPageableResponse {
    private List<RestaurantSimpleResponse> content;
    private int page;
    private int size;
    private long totalElements;
}
