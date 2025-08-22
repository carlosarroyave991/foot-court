package com.foorcourt.application.dto.response.dish;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishPageableResponse {
    private List<DishSimpleResponse> content;
    private int page;
    private int size;
    private long totalElements;
}
