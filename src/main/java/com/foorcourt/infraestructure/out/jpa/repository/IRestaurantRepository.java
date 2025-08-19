package com.foorcourt.infraestructure.out.jpa.repository;

import com.foorcourt.infraestructure.out.jpa.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findById(Long id);
    Optional<RestaurantEntity> findByNit(String nit);
}
