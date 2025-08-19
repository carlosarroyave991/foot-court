package com.foorcourt.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foorcourt.domain.model.simplemodel.DishSimpleModel;
import com.foorcourt.domain.model.simplemodel.OrderSimpleModel;
import com.foorcourt.infraestructure.out.jpa.entity.DishEntity;
import com.foorcourt.infraestructure.out.jpa.entity.OrderEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantModel {
    private Long id;
    private String name;
    private String address;
    private Long ownerId;
    private String phone;
    private String logoUrl;
    private String nit;

    @JsonIgnore
    private List<DishSimpleModel> dishes;

    @JsonIgnore
    private List<OrderSimpleModel> orders;
}
